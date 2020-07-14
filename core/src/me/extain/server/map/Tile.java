package me.extain.server.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.HashMap;

import me.extain.server.Physics.Box2DHelper;

public class Tile {

    public static TextureRegion wall = new TextureRegion(new Texture("tiles/WoodenWall2.png"));
    public static TextureRegion ground = new TextureRegion(new Texture("tiles/soil2.png"));
    public static TextureRegion ground1 = new TextureRegion(new Texture("tiles/soil.png"));

    private TextureRegion tileTexture;
    private static Vector2 position;
    private static HashMap<Vector2, Tile> tileList = new HashMap<Vector2, Tile>();

    private static int tileSize = 16;

    private static Body body;

    private static boolean isSolid = false;

    public Tile(TextureRegion tileTexture, Vector2 position, boolean isSolid) {
        this.tileTexture = tileTexture;
        this.position = position;

        tileList.put(position, this);

        this.isSolid = isSolid;
    }

    public static void generateSolid() {
        if (isSolid)
            body = Box2DHelper.createStaticBody(tileSize, tileSize, new Vector2(position.x, position.y));
    }

    public void render(SpriteBatch batch) {
        batch.draw(tileTexture, position.x, position.y, tileSize, tileSize);
    }

    public static Tile getTile(Vector2 position) {
        Tile tile = tileList.get(new Vector2(position.x * tileSize, position.y * tileSize));

        return tile;
    }

    public static Tile removeTile(Vector2 position) {
        tileList.remove(position);

        return null;
    }

    public TextureRegion getTileTexture() {
        return tileTexture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public static HashMap<Vector2, Tile> getTileList() {
        return tileList;
    }

    public static boolean getSolid() {
        return isSolid;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public static void setBody(Body body) {
        Tile.body = body;
    }

    public Tile destroyTile(Vector2 position) {
        if (body != null) {
            Box2DHelper.getWorld().destroyBody(body);
            body.setUserData(null);
            body = null;
            System.out.println("Destroyed body");
        }
        tileTexture = null;
        tileList.remove(position);

        return null;
    }

    public void dispose() {
        if (body != null)
            Box2DHelper.getWorld().destroyBody(body);
        tileTexture = null;
        tileList = null;
    }

}
