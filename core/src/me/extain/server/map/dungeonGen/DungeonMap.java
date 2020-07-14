package me.extain.server.map.dungeonGen;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import me.extain.server.GameObjectManager;
import me.extain.server.Player.Player;
import me.extain.server.map.BSP.Leaf;
import me.extain.server.map.Tile;

public class DungeonMap {

    public Tile[][] map, mapObj, map2;

    public int width, height;

    private Grid gridObj, grid2;



    public Leaf leaf;

    public GameObjectManager gameObjectManager;
    private Pixmap pixmap;
    private Texture texture;

    public DungeonMap(int width, int height) {
        this.map = new Tile[(int) width][(int) height];
        this.mapObj = new Tile[width][height];
        this.map2 = new Tile[width][height];
        this.width = width;
        this.height = height;

        gridObj = new Grid(width, height);
        grid2 = new Grid(width, height);

        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        gameObjectManager = new GameObjectManager();

        //leaf = new Leaf(0, 0, width, height);
        //leaf.create();

        //placeRooms();

        test2();
    }

    private void test2() {
        final Grid grid = new Grid(width, height);

        final MyDungeonGen dungeonGen = new MyDungeonGen();
        dungeonGen.generate(grid);

        //gameObjectManager.addGameObject(new Player(new Vector2(dungeonGen.getPlayerX() * 16, dungeonGen.getPlayerY() * 16)));

    }

    public void update(float deltaTime) {
        gameObjectManager.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = map[x][y];
                //Tile tile2 = mapObj[x][y];
                //Tile tile3 = map2[x][y];
                if (tile != null)
                    batch.draw(tile.getTileTexture(), x * 16, y * 16);
/*                if (tile2 != null)
                    batch.draw(tile2.getTileTexture(), x * 16, y * 16);
                if (tile3 != null)
                    batch.draw(tile3.getTileTexture(), x * 16, y * 16); */
            }
        }

        //batch.draw(texture, 10f, 10f);

        gameObjectManager.render(batch);
    }

    public Player getPlayer() {
        return gameObjectManager.getPlayer();
    }

}
