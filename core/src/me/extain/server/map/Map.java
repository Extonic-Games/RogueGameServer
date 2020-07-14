package me.extain.server.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.awt.Point;
import java.util.ArrayList;

public class Map {

    private ArrayList<Room> rooms;

    private int maxRooms, minRoomSize, maxRoomSize, mapWidth, mapHeight;

    private int tileSize = 16;

    private Tile[][] maps;

    private Vector2 playerSpawn;
    private Body body;

    public Map(int maxRooms, int minRoomSize, int maxRoomSize, int mapWidth, int mapHeight) {
        maps = new Tile[mapWidth * tileSize][mapHeight * tileSize];
        this.maxRooms = maxRooms;
        this.minRoomSize = minRoomSize;
        this.maxRoomSize = maxRoomSize;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void generateMap() {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                maps[i][j] = new Tile(Tile.wall, new Vector2(i, j), true);
            }
        }
        placeRooms();
        Tile.generateSolid();
    }

    public void render(SpriteBatch batch) {
            for (java.util.Map.Entry<Vector2, Tile> tiles : Tile.getTileList().entrySet()) {
                Vector2 position = tiles.getKey();
                Tile tile = tiles.getValue();

                batch.draw(tile.getTileTexture(), position.x * tileSize, position.y * tileSize, tileSize, tileSize);
            }

    }

    private void placeRooms() {
        System.out.println("Placing Rooms");
        rooms = new ArrayList<Room>();

        Room prevRoom = null;

        Point newCenter = null;

        for (int i = 0; i < maxRooms; i++) {

            int width = minRoomSize + MathUtils.random(maxRoomSize - minRoomSize + 1);
            int height = minRoomSize + MathUtils.random(maxRoomSize - minRoomSize + 1);
            int x = MathUtils.random(mapWidth - width);
            int y = MathUtils.random(mapHeight - height);

            Room newRoom = new Room(x * tileSize, y * tileSize, width, height);

            if (i == 0) {
                playerSpawn = new Vector2(newRoom.getCenter().x * tileSize, newRoom.getCenter().y * tileSize);
                System.out.println("Player Spawn: " + playerSpawn.x + " , " + playerSpawn.y);
            }

            System.out.println(newRoom.toString());

            boolean failed = false;

            for (Room otherRoom : rooms) {
                if (newRoom.intersects(otherRoom)) {
                    failed = true;
                    System.out.println("Failed creating a room at: " + newRoom.getX1() + " , " + newRoom.getY1());
                    //maxRooms += 1;
                    newRoom = null;
                    break;
                }
            }

                if (!failed) {
                    createRoom(newRoom);
                    newCenter = newRoom.getCenter();

                    if (rooms.size() != 0) {
                        Point prevCenter = rooms.get(rooms.size() - 1).getCenter();
                        //System.out.println("Previous Center: " + prevCenter.x + " , " + prevCenter.y);

                        if (MathUtils.random(2) == 1) {
                            hCorridor(prevCenter.x, newCenter.x, prevCenter.y);
                            vCorridor(prevCenter.y, newCenter.y, newCenter.x);
                        } else {
                            vCorridor(prevCenter.y, newCenter.y, prevCenter.x);
                            hCorridor(prevCenter.x, newCenter.x, newCenter.y);
                        }
                    }
                }

                if (!failed) {
                    rooms.add(newRoom);

                    System.out.println("Placed room at: " + newRoom.getX1() + " , " + newRoom.getY1());
                }
            }
    }

    private void hCorridor(int x1, int x2, int y) {
        for (int x = x1; x < Math.min(x1, x2) || x < Math.max(x1, x2) + 1; x++) {
            if (maps[x][y] != null && Tile.getTile(new Vector2(x, y)) != null) {
                //maps[x][y] = Tile.getTile(new Vector2(x, y)).destroyTile(new Vector2(x, y));
            }

                maps[x][y] = new Tile(Tile.ground, new Vector2(x, y), false);
        }
    }

    private void vCorridor(int y1, int y2, int x) {
        for (int y = y1; y < Math.min(y1, y2) || y < Math.max(y1, y2) + 1; y++) {
            if (maps[x][y] != null && Tile.getTile(new Vector2(x, y)) != null) {
                //maps[x][y] = Tile.getTile(new Vector2(x, y)).destroyTile(new Vector2(x, y));
            }

                maps[x][y] = new Tile(Tile.ground, new Vector2(x, y), false);
                //maps[x + 1][y + 1] = new Tile(Tile.wall, new Vector2(x, y), true);

        }
    }

    private void createRoom(Room room) {
        System.out.println("Creating room: " + room.toString());
        for (int x = room.getX1(); x < room.getXWidth(); x++) {
            for (int y = room.getY1(); y < room.getYWidth(); y++) {

                int roomX = x;
                int roomY = y;

                if (maps[roomX][roomY] != null && Tile.getTile(new Vector2(roomX, roomY)) != null) {
                    //maps[roomX][roomY] = Tile.getTile(new Vector2(roomX, roomY)).destroyTile(new Vector2(roomX, roomY));
                }
                maps[roomX][roomY] = new Tile(Tile.ground, new Vector2(roomX,roomY), false);
                //System.out.println("Placing tile at: " + (roomX) + " , " + (roomY));
            }
        }
    }

    public void dispose() {
        rooms = null;
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                Tile tile = maps[i][j];
                if (tile != null)
                    tile.dispose();
            }
        }
    }

    public Vector2 getPlayerSpawn() {
        return playerSpawn;
    }
}
