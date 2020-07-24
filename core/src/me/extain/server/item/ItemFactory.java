package me.extain.server.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.Hashtable;

import Utils.ConsoleLog;

public class ItemFactory {

    public Hashtable<String, Item> items;

    private final String ItemScript = "items/test.json";
    private static ItemFactory instance = null;

    public static ItemFactory instantiate() {

        if (instance == null) {
            instance = new ItemFactory();
        }

        return instance;
    }

    public ItemFactory () {
        Json json = new Json();
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(ItemScript));
        items = new Hashtable<>();

        for (JsonValue jsonVal : list) {
            Item item = json.readValue(Item.class, jsonVal);
            items.put(item.getItemTypeID(), item);
        }
    }

    public Item getItem(String name) {
        if (items.get(name) != null)
            return new Item(items.get(name));
        else
            return null;
    }

    public void reload() {
        Json json = new Json();
        items = new Hashtable<>();

        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(ItemScript));

        for (JsonValue jsonVal : list) {
            Item item = json.readValue(Item.class, jsonVal);
            items.put(item.getItemTypeID(), item);
        }
    }
}
