package me.extain.server.map.tiled;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import me.extain.server.objects.GameObjectFactory;
import me.extain.server.objects.GameObjectManager;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.objects.Player.Player;

public class TileMap {

    private TiledMap map;
    //private OrthogonalTiledMapRenderer renderer;
    private MapLayers mapLayers;

    private TileMapHelper tmHelper = new TileMapHelper();

    private Vector2 playerSpawn, slimeSpawn;

    private Array<Body> bodies;

    private GameObjectManager gameObjectManager;

    private GameObjectFactory factory;

    public TileMap(String tileMap) {
        this.map = new TmxMapLoader().load("maps/" + tileMap);
        this.mapLayers = map.getLayers();

        gameObjectManager = new GameObjectManager();
        factory = GameObjectFactory.instantiate();

        parseObjects();

        //this.renderer = new OrthogonalTiledMapRenderer(map);

        bodies = tmHelper.buildShapes(map, 16);
    }

    private void parseObjects() {
        MapObjects objects = mapLayers.get("Objects").getObjects();

        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (object.getName().equalsIgnoreCase("player_spawn")) {
                    playerSpawn = new Vector2(rectangle.x, rectangle.y);
                }

                if (object.getName().equalsIgnoreCase("slime_spawn")) {
                    slimeSpawn = new Vector2(rectangle.x, rectangle.y);
                    gameObjectManager.addGameObject(GameObjectFactory.instantiate().createObject("slime", slimeSpawn));
                } else if (object.getName().equalsIgnoreCase("octo_spawn")) {
                    slimeSpawn = new Vector2(rectangle.x, rectangle.y);
                    gameObjectManager.addGameObject(GameObjectFactory.instantiate().createObject("octo", slimeSpawn));
                }else if (object.getName().equalsIgnoreCase("night-crawler_spawn")) {
                    slimeSpawn = new Vector2(rectangle.x, rectangle.y);
                    gameObjectManager.addGameObject(GameObjectFactory.instantiate().createObject("nightcrawler", slimeSpawn));
                }else if (object.getName().equalsIgnoreCase("angry-blob_spawn")) {
                    slimeSpawn = new Vector2(rectangle.x, rectangle.y);
                    gameObjectManager.addGameObject(GameObjectFactory.instantiate().createObject("angryblob", slimeSpawn));
                }
            }
        }
    }

    public void update(float deltaTime) {
        gameObjectManager.update(deltaTime);
    }

    public void render() {

    }

    public void renderObjects(SpriteBatch batch) {
        //gameObjectManager.render(batch);
    }

    public void dispose() {
        this.map.dispose();
        for (Body body : bodies) {
            Box2DHelper.getWorld().destroyBody(body);
        }
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap tileMap) {
        this.map = tileMap;
    }


    public MapLayers getLayers() {
        return mapLayers;
    }

    public Vector2 getPlayerSpawn() {
        return playerSpawn;
    }

    public Vector2 getSlimeSpawn() {
        return slimeSpawn;
    }

    public Array<Body> getBodies() {
        return bodies;
    }

    public Player getPlayer() {
        return gameObjectManager.getPlayer();
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }

}
