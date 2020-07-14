package me.extain.server.map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import java.awt.Point;

public class Room extends Sprite {

    private int x1, x2, y1, y2, width, height;
    private int x, y;

    private Point center;

    public Room(int x, int y, int width, int height) {
        super();

        this.x1 = x;
        this.x2 = x + width;
        this.y1 = y;
        this.y2 = y + height;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        //this.setColor(Color.BLACK);
        //this.setTexture(Tile.ground);
        center = new Point((int) MathUtils.floor((x1 + x2) / 2), (int) MathUtils.floor((y1 + y2) / 2));
    }

    public boolean intersects(Room room) {
        return (x1 <= room.x2 && x2 >= room.x1 && y1 <= room.y2 && room.y2 >= room.y1);
    }

    public Point getCenter() {
        return center;
    }

    public String toString() {
        return "Position: " + this.getX1() + " , " + this.getY1() + " Size: " + this.width + " , " + this.height;
    }

    public int getX1() {
        return x;
    }

    public int getY1() {
        return y;
    }

    public int getXWidth() {
        return x2;
    }

    public int getYWidth() {
        return y2;
    }

}
