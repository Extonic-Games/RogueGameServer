package me.extain.server.network.command;

import com.esotericsoftware.kryonet.Connection;

import java.util.Map;

import me.extain.server.objects.Player.Account;
import me.extain.server.RogueGameServer;
import me.extain.server.packets.MessagePacket;

public class KickCommand implements Command{
    @Override
    public void init(Account account, String[] args) {
        if (args.length <= 1) {
            MessagePacket packet = new MessagePacket();
            packet.username = "[ORANGE]Server[]";
            packet.message = "Usage: /kick <player>";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);

            return;
        }

        String toKick = args[1];
        Account toKickAcc = null;
        Connection toKickConnection = null;

        if (!toKick.equalsIgnoreCase(account.getUsername())) {
            for (Map.Entry<Integer, Account> entry : RogueGameServer.getInstance().getAccounts().entrySet()) {
                if (entry.getValue().getUsername().equalsIgnoreCase(toKick)) {
                    toKickAcc = entry.getValue();

                    for (int i = 0; i < RogueGameServer.getInstance().getServer().getConnections().length; i++) {
                        if (RogueGameServer.getInstance().getServer().getConnections()[i].getID() == toKickAcc.getConnectionID()) {
                            toKickConnection = RogueGameServer.getInstance().getServer().getConnections()[i];
                            break;
                        }
                    }
                }
            }

            MessagePacket packet = new MessagePacket();
            packet.username = "Server";
            packet.message = "Successfully kicked player: " + toKickAcc.getUsername();

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);

            toKickConnection.close();
        }
    }

    @Override
    public String getName() {
        return "/kick";
    }

    @Override
    public int getPerm() {
        return 4;
    }
}
