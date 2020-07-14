package me.extain.server.packets;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class LootDropPacket {

    public int ownerID;
    public int id;

    public float x, y;

    public ArrayList<String> items;

}
