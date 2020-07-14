package me.extain.server;

import java.util.ArrayList;

public class ServerPlayer {

    public int id;
    public float x, y;
    public String username;
    public ArrayList<String> equipItems;
    public ArrayList<String> inventoryItems;

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void addInventoryItem(String itemName) {
        inventoryItems.add(itemName);
    }

    public void removeInventoryItem(String itemName) {
        inventoryItems.remove(itemName);
    }

    public void addEquipItem(String itemName) {
        equipItems.add(itemName);
    }

    public void removeEquipItem(String itemName) {
        equipItems.remove(itemName);
    }

    public ArrayList<String> getEquipItems() {
        return equipItems;
    }

    public ArrayList<String> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(ArrayList<String> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public void setEquipItems(ArrayList<String> equipItems) {
        this.equipItems = equipItems;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

}
