package me.extain.server.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.Hashtable;

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
        items = new Hashtable<String, Item>();

        for (JsonValue jsonVal : list) {
            Item item = json.readValue(Item.class, jsonVal);
            items.put(item.getItemTypeID(), item);
        }

    }

    public Item getItem(String name) {
        Item item = new Item(items.get(name));
        //TextureAtlas atlas = Assets.getInstance().getAssets().get("items/items.atlas");
        //item.setDrawable(new TextureRegionDrawable(atlas.findRegion(name)));
        return item;
    }
}
