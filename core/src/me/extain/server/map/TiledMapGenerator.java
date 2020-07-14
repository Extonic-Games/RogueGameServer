package me.extain.server.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;

public class TiledMapGenerator {

    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private Texture tiles;
    private Texture texture;

    public void generate() {
        tiles = new Texture("tiles/tiles.png");
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, 32, 32);
        map = new TiledMap();
        MapLayers layers = map.getLayers();

        for (int i = 0; i < 20; i++) {
            TiledMapTileLayer layer = new TiledMapTileLayer(150, 100, 32, 32);
            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 100; y++) {
                    int ty = (int)(MathUtils.random() * splitTiles.length);
                    int tx = (int)(MathUtils.random() * splitTiles[ty].length);
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
                    layer.setCell(x, y, cell);
                }
            }
            layers.add(layer);
        }

        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }
}
