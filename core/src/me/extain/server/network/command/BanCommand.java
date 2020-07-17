package me.extain.server.network.command;

import java.util.Map;

import me.extain.server.objects.Player.Account;
import me.extain.server.RogueGameServer;
import me.extain.server.db.DatabaseUtil;
import me.extain.server.packets.MessagePacket;

public class BanCommand implements Command {
    @Override
    public void init(Account account, String[] args) {
        if (args.length <= 1) {
            MessagePacket packet = new MessagePacket();
            packet.username = "[ORANGE]Server[]";
            packet.message = "Usage: /ban <player>";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);

            return;
        }

        String toBan = args[1];
        Account toBanAcc = null;

        if (!toBan.equalsIgnoreCase(account.getUsername())) {
            for (Map.Entry<Integer, Account> entry : RogueGameServer.getInstance().getAccounts().entrySet()) {
                if (entry.getValue().getUsername().equalsIgnoreCase(toBan)) {
                    toBanAcc = entry.getValue();
                }
            }

            if (toBanAcc != null) {
                toBanAcc.setBanned(true);
                DatabaseUtil.banPlayer(toBan);

                MessagePacket packet = new MessagePacket();
                packet.username = "[ORANGE]Server[]";
                packet.message = "Successfully banned player: " + toBan;

                RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
            } else {
                // Player is offline ban anyways!
                DatabaseUtil.banPlayer(toBan);

                MessagePacket packet = new MessagePacket();
                packet.username = "[ORANGE]Server[]";
                packet.message = "Successfully banned player: " + toBan;


                RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
            }
        }
    }

    @Override
    public String getName() {
        return "/ban";
    }

    @Override
    public int getPerm() {
        return 4;
    }
}
