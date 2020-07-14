package me.extain.server.network.command;

import java.util.Map;

import me.extain.server.Player.Account;
import me.extain.server.Player.Player;
import me.extain.server.RogueGameServer;
import me.extain.server.packets.MessagePacket;
import me.extain.server.packets.UpdatePacket;

public class TeleportCommand implements Command {
    @Override
    public void init(Account account, String[] args) {
        if (args.length <= 1) {
            MessagePacket packet = new MessagePacket();
            packet.username = "[ORANGE]Server[]";
            packet.message = "Usage: /tp <player>";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);

            return;
        }

        String teleTo = args[1];

       Player player = RogueGameServer.getInstance().getPlayers().get(account.getConnectionID());
       Player telePlayer = null;


        for (Map.Entry<Integer, Player> entry : RogueGameServer.getInstance().getPlayers().entrySet()) {
            if (entry.getValue().getUsername().equalsIgnoreCase(teleTo)) {
                telePlayer = entry.getValue();
            }
        }

        if (telePlayer != null) {
            player.getBody().setTransform(telePlayer.getPosition().x, telePlayer.getPosition().y, 0f);
            player.getBody().setAwake(true);
            MessagePacket packet = new MessagePacket();
            packet.username = "[ORANGE]Server[]";
            packet.message = "Teleporting to: " + telePlayer.getPosition();

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
        } else {
            MessagePacket packet = new MessagePacket();
            packet.username = "[ORANGE]Server[]";
            packet.message = "Invalid Target";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
        }
    }

    @Override
    public String getName() {
        return "/tp";
    }

    @Override
    public int getPerm() {
        return 6;
    }
}
