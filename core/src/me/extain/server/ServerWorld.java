package me.extain.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;

import me.extain.server.GameObjectManager;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.map.tiled.TileMap;

public class ServerWorld {

    public TileMap tileMap;
    private Server server;
    private GameObjectManager gameObjectManager;
    private Box2DHelper box2DHelper;

    public ServerWorld(Server server) {
        this.server = server;
        gameObjectManager = new GameObjectManager();
        box2DHelper = new Box2DHelper();
        box2DHelper.createWorld();
        tileMap = new TileMap("map2.tmx");
    }

    public void update() {
        if (!box2DHelper.getWorld().isLocked()) {
            tileMap.update(Gdx.graphics.getRawDeltaTime());

            gameObjectManager.update(Gdx.graphics.getRawDeltaTime());

            box2DHelper.step();
        }
    }

    public void render(float delta) {
    }

    public GameObjectManager getGameObjectManager() {
        return tileMap.getGameObjectManager();
    }

    public GameObjectManager gameObjectManager2() {
        return gameObjectManager;
    }

}
