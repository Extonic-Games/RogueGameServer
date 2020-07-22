package me.extain.server.packets;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import Utils.ConsoleLog;
import me.extain.server.Projectile.SwordSlash;
import me.extain.server.objects.GameObject;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.objects.Player.Account;
import me.extain.server.objects.Player.Player;
import me.extain.server.Projectile.Projectile;
import me.extain.server.Projectile.ProjectileFactory;
import me.extain.server.RogueGameServer;
import me.extain.server.ServerPlayer;
import me.extain.server.db.DatabaseUtil;

import me.extain.server.objects.Player.Character;
import me.extain.server.item.Item;
import me.extain.server.item.ItemFactory;
import me.extain.server.network.CommandHandler;

public class PacketHandler {

    private CommandHandler commandHandler = new CommandHandler();

    public void handleHello(Connection connection, HelloPacket packet) {
        HelloPacketACK helloPacketACK = new HelloPacketACK();
        connection.sendTCP(helloPacketACK);
    }

    public void handleRequestObj(Connection connection, Server server, RequestObjects packet) {
        // Get the current enemies in the world, and send it to the players.
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

        // Get the account the player is associated to.
        Account account = RogueGameServer.getInstance().getAccounts().get(connection.getID());
        int weapon = 0;

        // Check to see if they have a weapon equipped.
        if (account.getSelectedChar().getEquipItems().get(weapon) != null) {
            Item item = account.getSelectedChar().getEquipItems().get(weapon);

            // Check to see if the player can shoot.
            if (!item.getWeaponStats().getProjectile().equals("") && RogueGameServer.getInstance().getPlayers().get(connection.getID()).isCanShoot()) {
                // Update the player and let them know they just shot.
                ShootPacket sendShoot = RogueGameServer.getInstance().getPlayers().get(connection.getID()).shoot(packet, item);

                server.sendToAllUDP(sendShoot);
            }
        }
    }

    public void handleMessage(Connection connection, Server server, MessagePacket packet) {

        // Check to see if the message is a command.
        if (packet.message.startsWith("/")) {
            commandHandler.processCommand(connection, packet.message);
        } else {
            // Check to see if player isn't muted.
            if (!RogueGameServer.getInstance().getAccounts().get(connection.getID()).isMuted) {
                // Create the message packet, and check which rank they are.
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

                // Send the message packet.
                server.sendToAllUDP(message);
            } else {
                // Let them know they are muted.
                MessagePacket message = new MessagePacket();
                message.username = "[ORANGE]Server";
                message.message = "[]You are muted!";

                server.sendToUDP(connection.getID(), message);
            }
        }
    }

    public void handleLogin(Connection connection, Server server, LoginUserPacket packet) {

        // Check to see if the user has an account.
        if (DatabaseUtil.checkUser(packet.username, packet.password)) {
            // Check to see if the account isn't logged in currently.
            if (RogueGameServer.getInstance().getAccounts().get(packet.id) == null) {

                // Load the account from the database.
                Account account = new Account(DatabaseUtil.getUserID(packet.username, packet.password), packet.username, packet.email, packet.password);
                account.setConnectionID(connection.getID());
                account.setRank(DatabaseUtil.getRank(packet.username, packet.password));
                account.setMuted(DatabaseUtil.isMuted(packet.username));
                account.setBanned(DatabaseUtil.isBanned(packet.username));

                // Check to see if the user isn't banned.
                if (!account.isBanned) {
                    // Add the account into the list, and send the account to the user.
                    RogueGameServer.getInstance().addAcount(account.getConnectionID(), account);
                    LoginSuccessPacket successPacket = new LoginSuccessPacket();
                    successPacket.id = account.getId();
                    successPacket.username = account.getUsername();
                    successPacket.characters = account.getCharacters();
                    server.sendToTCP(connection.getID(), successPacket);
                } else {
                    // If the account is banned close the connection.
                    connection.close();
                }
            } else {
               ConsoleLog.logWarn("Account already logged in!");
            }
        } else {
            ConsoleLog.logError("Failed to log in user: " + packet.username);
        }
    }

    public void handleMove(Connection connection, Server server, MovePacket packet) {

        // Check to see if the current player isn't dead.
        if (!RogueGameServer.getInstance().getPlayers().get(connection.getID()).isDestroy) {
            // Update player position on the server.
            if (!Box2DHelper.getWorld().isLocked())
                RogueGameServer.getInstance().getPlayers().get(connection.getID()).getBody().setLinearVelocity(packet.x, packet.y);
            //Gdx.app.log("Server", "Player has moved: " + movePacket.x + " , " + movePacket.y);

            // Send the updated server position to the client.
            packet.id = connection.getID();
            packet.x = RogueGameServer.getInstance().getPlayers().get(connection.getID()).getPosition().x;
            packet.y = RogueGameServer.getInstance().getPlayers().get(connection.getID()).getPosition().y;
            server.sendToAllUDP(packet);
        }
    }

    public void handleJoin(Connection connection, Server server, JoinPacket packet) {
        // Create a new player packet
        NewPlayerPacket newPlayerPacket = new NewPlayerPacket();
        newPlayerPacket.serverPlayer = packet.player;

        // Create the player with the location sent from the client.
        Player player2 = new Player(new Vector2(packet.player.x, packet.player.y));
        player2.setID(packet.player.id);
        player2.setUsername(packet.player.username);

        // Send the new player to all other connections.
        server.sendToAllExceptTCP(connection.getID(), newPlayerPacket);

        // Send the other players to the one who just connected.
        for (Player player : RogueGameServer.getInstance().getPlayers().values()) {
            NewPlayerPacket packet2 = new NewPlayerPacket();
            ServerPlayer serverPlayer = new ServerPlayer();
            serverPlayer.setPosition(player.getPosition().x, player.getPosition().y);
            serverPlayer.setID(player.getID());
            serverPlayer.username = player.getUsername();
            packet2.serverPlayer = serverPlayer;
            connection.sendTCP(packet2);
        }

        // Add the player into the list of players and the world.
        RogueGameServer.getInstance().getAccounts().get(connection.getID()).setSelectedChar(packet.selectedChar);
        RogueGameServer.getInstance().addPlayer(connection.getID(), player2);
        RogueGameServer.getInstance().getServerWorld().gameObjectManager2().addGameObject(player2);

        // Send the current enemies to the player.
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
        Item item = ItemFactory.instantiate().getItem(packet.itemName);
        if (packet.isAdded) {
            if (packet.isEquipSlots) {

                /* Simple Check to see if the equip slot accepts that item. */
                if (packet.slotID == 0 && item.getItemUseType() == Item.ItemUseType.WEAPON.getValue()) {

                    RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().addEquipItem(packet.slotID, item);

                } else if (packet.slotID == 1 && item.getItemUseType() == Item.ItemUseType.ABILITY.getValue()) {

                    RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().addEquipItem(packet.slotID, item);

                } else if (packet.slotID == 2 && item.getItemUseType() == Item.ItemUseType.ARMOR_CHEST.getValue()) {

                    RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().addEquipItem(packet.slotID, item);

                } else if (packet.slotID == 3 && item.getItemUseType() == Item.ItemUseType.ARMOR_FEET.getValue()) {

                    RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().addEquipItem(packet.slotID, item);

                } else {
                    ConsoleLog.logError("Slot ID: " + packet.slotID + ", does not accept that item!");
                }


            }
            else
                RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().addInventoryItem(packet.slotID, item);
        } else {
            if (packet.isEquipSlots)
                RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().removeEquipItem(packet.slotID);
            else
                RogueGameServer.getInstance().getAccounts().get(connection.getID()).getSelectedChar().removeInventoryItem(packet.slotID);
        }
    }


}
