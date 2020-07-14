package me.extain.server.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Utils.ConsoleLog;
import me.extain.server.item.Item;

public class Character {

    public int accountID;
    public int id;

    public int charLevel;
    public float charExp;

    public ArrayList<String> equipItems;
    public ArrayList<String> inventoryItems;

    public Character() {
        equipItems = new ArrayList<>();
        inventoryItems = new ArrayList<>();
    }

    public Character(int id, int accountID) {
        equipItems = new ArrayList<>();
        inventoryItems = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public void setEquipItems(ArrayList<String> equipItems) {
        this.equipItems = equipItems;
    }

    public void setInventoryItems(ArrayList<String> inventoryItems) {
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

    public ArrayList<String> getEquipItems() {
        return equipItems;
    }

    public ArrayList<String> getInventoryItems() {
        return inventoryItems;
    }

    public void addEquipItem(String itemName) {
        equipItems.add(itemName);
    }

    public void addInventoryItem(String itemName) {
        inventoryItems.add(itemName);
    }

    public void removeEquipItem(String itemName) {
        ArrayList<String> removeItems = new ArrayList<>();
        for (String string : equipItems) {
            if (string.equalsIgnoreCase(itemName)) removeItems.add(string);
        }

        for (String string : removeItems) equipItems.remove(string);

        removeItems.clear();
    }

    public void removeInventoryItem(String itemName) {
        inventoryItems.remove(itemName);
        ConsoleLog.log(ConsoleLog.ANSI_BRIGHT_BLUE, String.join(", ", inventoryItems));
    }

    public int getId() {
        return id;
    }

    public int getAccountID() {
        return accountID;
    }
}
