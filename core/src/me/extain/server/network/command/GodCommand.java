package me.extain.server.network.command;

import me.extain.server.Player.Account;
import me.extain.server.Player.Player;
import me.extain.server.RogueGameServer;
import me.extain.server.packets.MessagePacket;

public class GodCommand implements Command {
    @Override
    public void init(Account account, String[] args) {
        Player player = RogueGameServer.getInstance().getPlayers().get(account.getConnectionID());

        player.setMaxHealth(10000);
        player.setHealth(10000);

        MessagePacket packet = new MessagePacket();
        packet.username = "[ORANGE]Server[]";
        packet.message = "You are now a god!";

        RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
    }

    @Override
    public String getName() {
        return "/god";
    }

    @Override
    public int getPerm() {
        return 6;
    }
}
