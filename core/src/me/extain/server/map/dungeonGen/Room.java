package me.extain.server.map.dungeonGen;

import com.badlogic.gdx.math.Vector2;

public class Room {

    private final int x, y, width, height;
    private Vector2 doorLoc;

    public Room(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void create(Grid grid) {

        for (int x = this.x + 1; x < this.x + this.width; x++) {
            for (int y = this.y; y < this.y + this.height; y++) {
                grid.set(x, y, 0.5f);
            }
        }

        for (int x = this.x + 1; x < this.x + this.width; x++) { // Bottom
            grid.set(x, this.y, 0.4f);
        }

        for (int y = this.y + 1; y < this.y + this.height + 1; y++) { // Left
            grid.set(this.x, y, 0.3f);
        }

        for (int y = this.y + 1; y < this.y + this.height + 1; y++) { // Right
            grid.set(this.x + this.width, y, 0.1f);
        }

        for (int x = this.x + 1; x < this.x + this.width; x++) { // Top
            grid.set(x, this.y + this.height, 0.6f);
        }

        grid.set(x, y, 0.7f);
        grid.set(x + width, y, 0.8f);


    }

    public boolean overlaps(final Room room) {
        return x < room.x + room.width && x + width > room.x && y < room.y + room.height && y + height > room.y;
    }

    public Vector2 getDoorLoc() {
        return doorLoc;
    }

    public void setDoorLoc(Vector2 doorLoc) {
        this.doorLoc = doorLoc;
    }

    public void setDoorLoc(float x, float y) {
        doorLoc.set(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
