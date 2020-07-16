package me.extain.server.packets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.util.Queue;

import javax.xml.crypto.Data;

import Utils.ConsoleLog;
import me.extain.server.GameObject;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.Player.Account;
import me.extain.server.Player.Player;
import me.extain.server.Projectile.Projectile;
import me.extain.server.Projectile.ProjectileFactory;
import me.extain.server.RogueGameServer;
import me.extain.server.ServerPlayer;
import me.extain.server.db.DatabaseUtil;

import me.extain.server.Player.Character;
import me.extain.server.item.Item;
import me.extain.server.item.ItemFactory;
import me.extain.server.network.CommandHandler;

public class PacketHandler {

    private CommandHandler commandHandler = new CommandHandler();

    public void handleHello(Connection connection, HelloPacket packet) {
        System.out.println(packet.getMessage());

        HelloPacketACK helloPacketACK = new HelloPacketACK();
        connection.sendTCP(helloPacketACK);
    }

    public void handleRequestObj(Connection connection, Server server, RequestObjects packet) {
        for (GameObject gameObject : RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects()) {
            if (!(gameObject instanceof Projectile)) {
                SendObjectsPacket sendObjectsPacket = new SendObjectsPacket();
                sendObjectsPacket.id = gameObject.getID();
                sendObjectsPacket.name = gameObject.getName();
                sendObjectsPacket.health = gameObject.getHealth();
                sendObjectsPacket.x = gameObject.getPosition().x;
                sendObjectsPacket.y = gameObject.getPosition().y;
                server.sendToUDP(connection.getID(), sendObjectsPacket);
            }
        }
    }

    public void handleShootPacket(Connection connection, Server server, ShootPacket packet) {
        Account account = RogueGameServer.getInstance().getAccounts().get(connection.getID());
        int weapon = 0;

        if (!account.getSelectedChar().getEquipItems().get(weapon).isEmpty()) {
            Item item = ItemFactory.instantiate().getItem(account.getSelectedChar().getEquipItems().get(weapon));

            if (!item.getWeaponStats().getProjectile().equals("")) {
                Projectile projectile = ProjectileFactory.getInstance().getProjectile(item.getWeaponStats().getProjectile(), new Vector2(packet.x, packet.y), new Vector2(packet.velX, packet.velY), packet.mask);
                projectile.setMinDamage(item.getWeaponStats().getDamage());
                projectile.setMaxDamage(item.getWeaponStats().getMaxDamage());
                packet.name = item.getWeaponStats().getProjectile();
                packet.damage = projectile.getDamageRange();
                projectile.shooterID = packet.id;
                if (!Box2DHelper.getWorld().isLocked())
                    RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects().add(projectile);
                server.sendToAllUDP(packet);
            }
        }
    }

    public void handleMessage(Connection connection, Server server, MessagePacket packet) {

        if (packet.message.startsWith("/")) {
            commandHandler.processCommand(connection, packet.message);
        } else {
            if (!RogueGameServer.getInstance().getAccounts().get(connection.getID()).isMuted) {
                MessagePacket message = new MessagePacket();
                if (RogueGameServer.getInstance().getAccounts().get(connection.getID()).getRank() == 6) {
                    message.username = "[CYAN]" + packet.username;
                    message.message = "[]" + packet.message;
                } else if (RogueGameServer.getInstance().getAccounts().get(connection.getID()).getRank() == 4) {
                    message.username = "[GREEN]" + packet.username;
                    message.message = "[]" + packet.message;
                } else {
                    message.username = packet.username;
                    message.message = packet.message;
                }

                server.sendToAllUDP(message);
            } else {
                MessagePacket message = new MessagePacket();
                message.username = "[ORANGE]Server";
                message.message = "[]You are muted!";

                server.sendToUDP(connection.getID(), message);
            }
        }
    }

    public void handleLogin(Connection connection, Server server, LoginUserPacket packet) {
        if (DatabaseUtil.checkUser(packet.username, packet.password)) {
            if (RogueGameServer.getInstance().getAccounts().get(packet.id) == null) {
                Account account = new Account(DatabaseUtil.getUserID(packet.username, packet.password), packet.username, packet.email, packet.password);
                account.setConnectionID(connection.getID());
                account.setRank(DatabaseUtil.getRank(packet.username, packet.password));
                account.setMuted(DatabaseUtil.isMuted(packet.username));
                account.setBanned(DatabaseUtil.isBanned(packet.username));

                if (!account.isBanned) {
                    RogueGameServer.getInstance().addAcount(account.getConnectionID(), account);
                    LoginSuccessPacket successPacket = new LoginSuccessPacket();
                    successPacket.id = account.getId();
                    successPacket.username = account.getUsername();
                    successPacket.characters = account.getCharacters();
                    server.sendToTCP(connection.getID(), successPacket);
                } else {
                    connection.close();
                    ConsoleLog.logWarn("Player: " + packet.username + ", is banned!");
                }
            } else {
               ConsoleLog.logWarn("Account already logged in!");
            }
        } else {
            ConsoleLog.logError("Failed to log in user: " + packet.username);
        }
    }

    public void handleMove(Connection connection, Server server, MovePacket packet) {
        if (!RogueGameServer.getInstance().getPlayers().get(connection.getID()).isDestroy) {
            if (!Box2DHelper.getWorld().isLocked())
                RogueGameServer.getInstance().getPlayers().get(connection.getID()).getBody().setLinearVelocity(packet.x, packet.y);
            //Gdx.app.log("Server", "Player has moved: " + movePacket.x + " , " + movePacket.y);

            packet.id = connection.getID();
            packet.x = RogueGameServer.getInstance().getPlayers().get(connection.getID()).getPosition().x;
            packet.y = RogueGameServer.getInstance().getPlayers().get(connection.getID()).getPosition().y;
            server.sendToAllUDP(packet);
        }
    }

    public void handleJoin(Connection connection, Server server, JoinPacket packet) {
        NewPlayerPacket newPlayerPacket = new NewPlayerPacket();
        newPlayerPacket.serverPlayer = packet.player;
        Player player2 = new Player(new Vector2(packet.player.x, packet.player.y));
        player2.setID(packet.player.id);
        player2.setUsername(packet.player.username);
        server.sendToAllExceptTCP(connection.getID(), newPlayerPacket);

        for (Player player : RogueGameServer.getInstance().getPlayers().values()) {
            NewPlayerPacket packet2 = new NewPlayerPacket();
            ServerPlayer serverPlayer = new ServerPlayer();
            serverPlayer.setPosition(player.getPosition().x, player.getPosition().y);
            serverPlayer.setID(player.getID());
            serverPlayer.username = player.getUsername();
            packet2.serverPlayer = serverPlayer;
            connection.sendTCP(packet2);
        }

        RogueGameServer.getInstance().getAccounts().get(connection.getID()).setSelectedChar(packet.selectedChar);
        RogueGameServer.getInstance().addPlayer(connection.getID(), player2);
        RogueGameServer.getInstance().getServerWorld().gameObjectManager2().addGameObject(player2);

        for (GameObject gameObject : RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects()) {
            if (!(gameObject instanceof Projectile)) {
                SendObjectsPacket sendObjectsPacket = new SendObjectsPacket();
                sendObjectsPacket.id = gameObject.getID();
                sendObjectsPacket.name = gameObject.getName();
                sendObjectsPacket.health = gameObject.getHealth();
                sendObjectsPacket.x = gameObject.getPosition().x;
                sendObjectsPacket.y = gameObject.getPosition().y;
                server.sendToUDP(connection.getID(), sendObjectsPacket);
            }
        }
    }

    public void handleNewCharacter(Connection connection, Server server, NewCharacterPacket packet) {
        Character character = DatabaseUtil.createNewCharacter(packet.accountID);

        RogueGameServer.getInstance().getAccounts().get(connection.getID()).addCharacter(character);

        NewCharacterAckPacket packet1 = new NewCharacterAckPacket();
        packet1.accountID = packet.accountID;
        packet1.character = character;

        server.sendToTCP(connection.getID(), packet1);
    }

    public void handleInventoryUpdatePacket(Connection connection, Server server, InventoryUpdatePacket packet) {
        if (packet.isAdded) {
            if (packet.isEquipSlots)
                RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().addEquipItem(packet.slotID, packet.itemName);
            else
                RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().addInventoryItem(packet.slotID, packet.itemName);
        } else {
            if (packet.isEquipSlots)
                RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().removeEquipItem(packet.slotID, packet.itemName);
            else
                RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().removeInventoryItem(packet.slotID, packet.itemName);
        }
    }


}
