package me.extain.server.network.command;

import java.util.Map;

import me.extain.server.objects.Player.Account;
import me.extain.server.RogueGameServer;
import me.extain.server.db.DatabaseUtil;
import me.extain.server.network.packets.MessagePacket;

public class MuteCommand implements Command{
    @Override
    public void init(Account account, String[] args) {
        if (args.length <= 1) {
            MessagePacket packet = new MessagePacket();
            packet.username = "Server";
            packet.message = "Usage: /mute <player>";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);

            return;
        }

        String toMute = args[1];
        Account toMuteAcc = null;

        if (!toMute.equalsIgnoreCase(account.getUsername())) {
            for (Map.Entry<Integer, Account> entry : RogueGameServer.getInstance().getAccounts().entrySet()) {
                if (entry.getValue().getUsername().equalsIgnoreCase(toMute)) {
                    toMuteAcc = entry.getValue();
                }
            }

            if (toMuteAcc != null) {
                toMuteAcc.setMuted(true);
                DatabaseUtil.mutePlayer(toMute);

                MessagePacket packet = new MessagePacket();
                packet.username = "Server";
                packet.message = "Successfully muted player: " + toMute;

                RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
            } else {
                MessagePacket packet = new MessagePacket();
                packet.username = "Server";
                packet.message = "Player not found: " + toMute;

                RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
            }
        }
    }

    @Override
    public String getName() {
        return "/mute";
    }

    @Override
    public int getPerm() {
        return 4;
    }
}
