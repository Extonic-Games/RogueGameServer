package me.extain.server.objects.Player;

import java.util.HashMap;

import me.extain.server.item.Item;

public class Character {

    private int accountID;
    public int id;

    private int charLevel;
    private float charExp;

    private HashMap<Integer, Item> equipItems;
    private HashMap<Integer, Item> inventoryItems;

    public Character() {
        equipItems = new HashMap<>();
        inventoryItems = new HashMap<>();
    }

    public Character(int id, int accountID) {
        equipItems = new HashMap<>();
        inventoryItems = new HashMap<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public void setEquipItems(HashMap<Integer, Item> equipItems) {
        this.equipItems = equipItems;
    }

    public void setInventoryItems(HashMap<Integer, Item> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public void setCharExp(float charExp) {
        this.charExp = charExp;
    }

    public void setCharLevel(int charLevel) {
        this.charLevel = charLevel;
    }

    public float getCharExp() {
        return charExp;
    }

    public int getCharLevel() {
        return charLevel;
    }

    public HashMap<Integer, Item> getEquipItems() {
        return equipItems;
    }

    public HashMap<Integer, Item> getInventoryItems() {
        return inventoryItems;
    }

    public void addEquipItem(int slot, Item itemName) {
        equipItems.put(slot, itemName);
    }

    public void addInventoryItem(int slot, Item itemName) {
        inventoryItems.put(slot, itemName);
    }

    public void removeEquipItem(int slot) {
        equipItems.remove(slot);
    }

    public void removeInventoryItem(int slot) {
        inventoryItems.remove(slot);
    }

    public int getId() {
        return id;
    }

    public int getAccountID() {
        return accountID;
    }
}
