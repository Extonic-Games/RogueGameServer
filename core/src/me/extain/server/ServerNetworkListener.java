package me.extain.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import me.extain.server.Player.Player;
import me.extain.server.db.DatabaseUtil;
import me.extain.server.packets.HelloPacket;
import me.extain.server.packets.InventoryUpdatePacket;
import me.extain.server.packets.JoinPacket;
import me.extain.server.packets.LoginUserPacket;
import me.extain.server.packets.MessagePacket;
import me.extain.server.packets.MovePacket;
import me.extain.server.packets.NewCharacterPacket;
import me.extain.server.packets.NewPlayerPacket;
import me.extain.server.packets.PacketHandler;
import me.extain.server.packets.PlayerDisconnected;
import me.extain.server.packets.RequestObjects;
import me.extain.server.packets.ShootPacket;


public class ServerNetworkListener extends Listener {


    private Server server;
    private PacketHandler packetHandler;

    public ServerNetworkListener(Server server) {
        this.server = server;
        packetHandler = new PacketHandler();
        //simulatePlayers(100);
    }

    public void connected(Connection c) {

    }

    public void simulatePlayers(int amt) {
        for (int i = 0; i < amt; i++) {
            ServerPlayer player = new ServerPlayer();

            player.setPosition((MathUtils.random(RogueGameServer.getInstance().getServerWorld().tileMap.getPlayerSpawn().x, RogueGameServer.getInstance().getServerWorld().tileMap.getPlayerSpawn().x + i)), RogueGameServer.getInstance().getServerWorld().tileMap.getPlayerSpawn().y);
            Player player2 = new Player(new Vector2(player.getX(), player.getY()));
            RogueGameServer.getInstance().addPlayer(i + 5, player2);
        }
    }

    public void disconnected(Connection c) {
        RogueGameServer.getInstance().removePlayer(c.getID());
        PlayerDisconnected playerDisconnected = new PlayerDisconnected();
        playerDisconnected.id = c.getID();
        if (RogueGameServer.getInstance().getAccounts().get(c.getID()) != null) {
            DatabaseUtil.saveCharacter(RogueGameServer.getInstance().getAccounts().get(c.getID()).getSelectedChar());
            RogueGameServer.getInstance().removeAccount(c.getID());
        }
        server.sendToAllExceptTCP(c.getID(), playerDisconnected);
        System.out.println("Player: " + c.getID() + " has disconnected!");
    }

    @Override
    public void received(Connection connection, Object object) {
        Gdx.app.postRunnable(() -> {
            if (object instanceof HelloPacket) {
                HelloPacket helloPacket = (HelloPacket) object;
                packetHandler.handleHello(connection, helloPacket);
            }

            if (object instanceof JoinPacket) {
                JoinPacket joinPacket = (JoinPacket) object;
                packetHandler.handleJoin(connection, server, joinPacket);
            }

            if (object instanceof MovePacket) {
                MovePacket movePacket = (MovePacket) object;

                packetHandler.handleMove(connection, server, movePacket);

/*         MovePacketACK movePacketACK = new MovePacketACK();
        movePacketACK.x = movePacket.x;
        movePacketACK.y = movePacket.y;
        connection.sendTCP(movePacketACK); */
            }

            if (object instanceof RequestObjects) {
                RequestObjects requestObjects = (RequestObjects) object;

                packetHandler.handleRequestObj(connection, server, requestObjects);
            }

            if (object instanceof ShootPacket) {
                ShootPacket packet = (ShootPacket) object;
                packetHandler.handleShootPacket(connection, server, packet);
            }

            if (object instanceof LoginUserPacket) {
                LoginUserPacket packet = (LoginUserPacket) object;
                packetHandler.handleLogin(connection, server, packet);
            }

            if (object instanceof MessagePacket) {
                MessagePacket packet = (MessagePacket) object;
                packetHandler.handleMessage(connection, server, packet);
            }

            if (object instanceof NewCharacterPacket) {
                packetHandler.handleNewCharacter(connection, server, (NewCharacterPacket) object);
            }

            if (object instanceof InventoryUpdatePacket) {
                packetHandler.handleInventoryUpdatePacket(connection, server, (InventoryUpdatePacket) object);
            }

        });
    }

    @Override
    public void idle(Connection connection) {

    }
}
