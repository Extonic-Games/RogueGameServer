package me.extain.server.network.command;

import me.extain.server.RogueGameServer;
import me.extain.server.item.Item;
import me.extain.server.item.ItemFactory;
import me.extain.server.objects.Player.Account;
import me.extain.server.objects.Player.Character;
import me.extain.server.network.packets.InventoryUpdatePacket;
import me.extain.server.network.packets.MessagePacket;

public class GiveCommand implements Command {
    @Override
    public void init(Account account, String[] args) {
        Character character = account.getSelectedChar();

        if (args.length < 1) {
            MessagePacket packet = new MessagePacket();
            packet.username = "[ORANGE]Server[]";
            packet.message = "Usage: /give <itemName>";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);

            return;
        }

        String item = args[1];

        Item spawnItem = ItemFactory.instantiate().getItem(item);

        if (spawnItem != null) {
            int slot = character.getOpenInvSlot();

            if (slot != -1) {
                character.addInventoryItem(slot, spawnItem);

                MessagePacket packet = new MessagePacket();
                packet.username = "[ORANGE]Server[]";
                packet.message = "Successfully gave item: " + item;

                RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);

                InventoryUpdatePacket packet1 = new InventoryUpdatePacket();
                packet1.slotID = slot;
                packet1.itemName = item;
                packet1.isEquipSlots = false;
                packet1.isAdded = true;
                packet1.accountID = account.getId();

                RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet1);
            } else {
                MessagePacket packet = new MessagePacket();
                packet.username = "[ORANGE]Server[]";
                packet.message = "No open inventory spaces!";

                RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
            }
        }else {
            MessagePacket packet = new MessagePacket();
            packet.username = "[ORANGE]Server[]";
            packet.message = "Item: " + item + ", not found!";

            RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
        }
    }

    @Override
    public String getName() {
        return "/give";
    }

    @Override
    public int getPerm() {
        return 6;
    }
}
