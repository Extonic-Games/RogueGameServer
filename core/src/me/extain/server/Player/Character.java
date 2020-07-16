package me.extain.server.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import Utils.ConsoleLog;
import me.extain.server.item.Item;

public class Character {

    public int accountID;
    public int id;

    public int charLevel;
    public float charExp;

    public HashMap<Integer, String> equipItems;
    public HashMap<Integer, String> inventoryItems;

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

    public void setEquipItems(HashMap<Integer, String> equipItems) {
        this.equipItems = equipItems;
    }

    public void setInventoryItems(HashMap<Integer, String> inventoryItems) {
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

    public HashMap<Integer, String> getEquipItems() {
        return equipItems;
    }

    public HashMap<Integer, String> getInventoryItems() {
        return inventoryItems;
    }

    public void addEquipItem(int slot, String itemName) {
        equipItems.put(slot, itemName);
    }

    public void addInventoryItem(int slot, String itemName) {
        inventoryItems.put(slot, itemName);
    }

    public void removeEquipItem(int slot, String itemName) {
        equipItems.remove(slot, itemName);
    }

    public void removeInventoryItem(int slot, String itemName) {
        inventoryItems.remove(slot, itemName);
    }

    public int getId() {
        return id;
    }

    public int getAccountID() {
        return accountID;
    }
}
