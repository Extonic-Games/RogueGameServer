package me.extain.server.network.packets;

public class InventoryUpdatePacket {

    public int accountID;
    public boolean isEquipSlots;
    public int slotID;
    public String itemName;
    public boolean isAdded;

}
