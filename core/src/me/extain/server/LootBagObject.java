package me.extain.server;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import me.extain.server.Physics.Box2DHelper;
import me.extain.server.objects.GameObject;

public class LootBagObject extends GameObject {

    public ArrayList<String> items;

    public LootBagObject(Vector2 position) {
        super(position, Box2DHelper.createSensorCircle(position, 2f, Box2DHelper.BIT_ITEM));

        items = new ArrayList<>();

        this.getBody().setUserData(this);
    }

    public void update(float deltaTime) {
        if (items.size() < 0) this.isDestroy = true;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void removeItem(String item) {
        items.remove(item);
    }
}
