package me.extain.server.network.command;

import java.util.Map;

import me.extain.server.objects.Player.Account;
import me.extain.server.RogueGameServer;
import me.extain.server.db.DatabaseUtil;
import me.extain.server.network.packets.MessagePacket;

public class RankCommand implements Command {

    @Override
    public void init(Account account, String[] args) {
        if (args.length <= 2) {
            MessagePacket packet = new MessagePacket();
            packet.username = "Server";
            packet.message = "Usage: /rank <player> <rank>";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
        }


        int rank = 0;
        String toRank = args[1];
        Account toRankAcc = null;

        try {
            rank = Integer.parseInt(args[2]);
        } catch(NumberFormatException e) {
            MessagePacket packet = new MessagePacket();
            packet.username = "Server";
            packet.message = "Usage: /rank <player> <rank>";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);

            return;
        }

        for (Map.Entry<Integer, Account> entry : RogueGameServer.getInstance().getAccounts().entrySet()) {
            if (entry.getValue().getUsername().equalsIgnoreCase(args[1])) {
                toRankAcc = entry.getValue();
            }
        }

        if (toRankAcc != null) {
            toRankAcc.setRank(rank);
            DatabaseUtil.rankPlayer(toRank, rank);

            MessagePacket packet = new MessagePacket();
            packet.username = "Server";
            packet.message = "Successfully changed the rank of: " + toRank;

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
        } else {
            DatabaseUtil.rankPlayer(toRank, rank);

            MessagePacket packet = new MessagePacket();
            packet.username = "Server";
            packet.message = "Successfully changed the rank of: " + toRank;

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
        }



    }

    @Override
    public String getName() {
        return "/rank";
    }

    @Override
    public int getPerm() {
        return 6;
    }
}
