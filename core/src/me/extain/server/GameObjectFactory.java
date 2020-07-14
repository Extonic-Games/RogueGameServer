package me.extain.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;

import me.extain.server.Behaviors.ChaseBehavior;
import me.extain.server.Behaviors.ShootBehavior;
import me.extain.server.Physics.Box2DHelper;


public class GameObjectFactory {

    private static HashMap<String, GameObjectWrapper> gameObjects;
    //private ArrayList<GameObjectWrapper> objects;

    private static GameObjectFactory instance = null;

    public static GameObjectFactory instantiate() {
        if (instance == null) instance = new GameObjectFactory();

        return instance;
    }

    public GameObjectFactory() {
        Json json = new Json();
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal("entities/test.json"));
        gameObjects = new HashMap<>();

        for (JsonValue jsonValue : list) {
            GameObjectWrapper wrapper = json.readValue(GameObjectWrapper.class, jsonValue);
            gameObjects.put(wrapper.name, wrapper);
        }
    }

    public GameObjectFactory(String name, String atlas, float health, float size, String type, ArrayList<String> behaviors, String projectile) {
        GameObjectWrapper wrapper = new GameObjectWrapper();
        gameObjects = new HashMap<String, GameObjectWrapper>();

        //context.getAssetManager().load("entities/" + atlas, TextureAtlas.class);

        wrapper.name = name;
        wrapper.health = health;
        wrapper.type = type;
        wrapper.size = size;
        wrapper.behaviors = behaviors;

        wrapper.projectile = projectile;

        if (gameObjects.get(name) == null)
            gameObjects.put(name, wrapper);

        System.out.println(gameObjects.toString());
    }

    public static GameObject createObject(String name, Vector2 pos) {
        GameObjectWrapper wrapper = gameObjects.get(name);

        GameObject object = new GameObject(wrapper, pos, Box2DHelper.createDynamicBodyCircle(pos, wrapper.size / 3, Box2DHelper.BIT_ENEMY));

        if (object != null) {
            object.setID(MathUtils.random(1000));

            BehaviorTreeLibraryManager btlm = BehaviorTreeLibraryManager.getInstance();
            BehaviorTree btree = btlm.createBehaviorTree("entities/btree/test.btree", object);
            object.setBehaviorTree(btree);
            object.createEyes();

            return object;
        }

        return null;
    }
}