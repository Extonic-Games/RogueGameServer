package me.extain.server.map.dungeonGen;

public class Grid {

    private final float[][] grid;

    private final int width, height;

    public Grid(final int width, final int height) {
        this.grid = new float[width][height];
        this.width = width;
        this.height = height;
    }

    public float get(int x, int y) {
        return grid[x][y];
    }

    public void set(int x, int y, float value) {
        this.grid[x][y] = value;
    }

    public void fill(float value) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = value;
            }
        }
    }

    public float[][] getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
