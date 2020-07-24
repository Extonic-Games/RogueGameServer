package me.extain.server.network.command;

import me.extain.server.objects.Player.Account;
import me.extain.server.RogueGameServer;
import me.extain.server.network.packets.MessagePacket;

public class TestCommand implements Command {
    @Override
    public void init(Account account, String[] args) {
        MessagePacket packet = new MessagePacket();
        packet.username = "[ORANGE]Server[]";
        packet.message = "This is a test command!";

        RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
    }

    @Override
    public String getName() {
        return "/test";
    }

    @Override
    public int getPerm() {
        return 0;
    }
}
