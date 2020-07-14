package me.extain.server.map.dungeonGen;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class MyDungeonGen {

    private Grid grid;
    private ArrayList<Room> rooms = new ArrayList<Room>();

    private int minRoomSize = 7;
    private int maxRoomSize = 11;

    private boolean isBase = true;

    private int playerX;
    private int  playerY;

    public void generate(Grid grid) {
        this.grid = grid;

        grid.fill(0f);

        spawnRooms(grid);
        generateDoors(grid);

    }

    private void spawnRooms(Grid grid) {
        for (int i = 0, maxRooms = 20; i < 200; i++) {
            final Room newRoom = randomRoom(grid, isBase);
            isBase = false;

            if (!overlaps(newRoom) && !outsideMap(newRoom)) {
                rooms.add(newRoom);
                newRoom.create(grid);

                System.out.println("Room created at: " + newRoom.getX() + " , " + newRoom.getY());
            }

            if (rooms.size() < maxRooms) maxRooms -= rooms.size();

        }
    }

    private boolean outsideMap(Room room) {
        if (room.getX() + room.getWidth() >= grid.getWidth() || room.getY() + room.getHeight() >= grid.getHeight() || room.getX() <= 0 || room.getY() <= 0) return true;
        else return false;
    }

    private Room randomRoom(Grid grid, boolean isBase) {
        final int width = randomSize();
        final int height = randomSize(width);

        int x = 0;
        int y = 0;

        if (isBase) {
            x = (grid.getWidth() / 2) - width;
            y = 10;
            playerX = x;
            playerY = y;
        }
            for (Room room : rooms) {
                if (room != null) {
                    if (MathUtils.randomBoolean(0.5f)) { // Place room left if is less than 50% otherwise place to the right
                        if ((room.getX() - width) - 1 > 0) {
                            x = room.getX() - width - 1;
                            y = room.getY();
                        }
                    } else {
                        if ((room.getX() + room.getWidth() + 1) + width < grid.getWidth()) {
                            x = room.getX() + room.getWidth() + 1;
                            y = room.getY();
                        }
                    }

                    if (MathUtils.randomBoolean(0.3f)) { // Place room top if is less than 50% otherwise place below
                        if ((room.getY() + room.getHeight() + height) + 1 < grid.getHeight()) {
                            y = room.getY() + room.getHeight() + 1;
                            x = room.getX();
                        }
                    } else {
                        if (room.getY() - height > 0) {
                            y = room.getY() - height - 1;
                            x = room.getX();
                        }
                    }
                }
            }


        return new Room(x, y, width, height);
    }

    private void generateDoors(Grid grid) {

        for (Room room : rooms) {
            int x = room.getX();
            int y = room.getY();

            if (grid.get(x, y + 2) == 0.1f && grid.get(x, y - 2) == 0.5f) // Left
                grid.set(x, y + 2, 0.5f);

            if (grid.get(x + room.getWidth() + 2, y + 2) == 0.3f || grid.get(x + room.getWidth() + 2, y + 2) == 0.1f && grid.get(x + room.getWidth() + 2, y + 2) == 0.5f)
                grid.set(x + room.getWidth() + 2, y + 2, 0.5f);

            if (grid.get(x + 2, y + room.getHeight() + 1) == 0.4f || grid.get(x + 2, y + room.getHeight() + 1) == 0.6f && grid.get(x + 2, y + room.getHeight() + 2) == 0.5f)
                grid.set(x + 2, y + room.getHeight(), 0.5f);

            if (grid.get(x + 2, y) == 0.1f || grid.get(x + 2, y - 1) == 0.5f || grid.get(x + 2, y - 2) == 0.6f)
                grid.set(x + 2, y, 0.5f);

            if (grid.get(x + 2, y - 1) == 0.1f || grid.get(x + 2, y - 1) == 0.5f || grid.get(x + 2, y - 2) == 0.6f)
                grid.set(x + 2, y - 1, 0.5f);
        }

    }

    private int randomSize() {
        return MathUtils.random(minRoomSize, maxRoomSize);
    }

    private int randomSize(float bound) {
        return (int) MathUtils.random(Math.max(minRoomSize, bound),
                Math.min(maxRoomSize, bound));
    }

    private boolean overlaps(Room room) {
        for (Room curRoom : rooms) {
            if (curRoom.overlaps(room)) {
                return true;
            }
        }

        return false;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

}
