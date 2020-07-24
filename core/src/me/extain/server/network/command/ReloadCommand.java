package me.extain.server.network.command;

import me.extain.server.objects.Player.Account;
import me.extain.server.RogueGameServer;
import me.extain.server.item.ItemFactory;
import me.extain.server.network.packets.MessagePacket;

public class ReloadCommand implements Command {
    @Override
    public void init(Account account, String[] args) {
        ItemFactory.instantiate().reload();

        MessagePacket packet = new MessagePacket();
        packet.username = "Server";
        packet.message = "Reloaded items!";

        RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
    }

    @Override
    public String getName() {
        return "/reload";
    }

    @Override
    public int getPerm() {
        return 6;
    }
}
