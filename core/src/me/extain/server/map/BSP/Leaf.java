package me.extain.server.map.BSP;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.Point;
import java.util.ArrayList;

public class Leaf {

    private final int MIN_LEAF_SIZE = 6;
    public int y, x, width, height;

    public Leaf leftChild, rightChild;
    public Rectangle room;
    public ArrayList<Rectangle> rooms = new ArrayList<>();
    public ArrayList<Rectangle> halls;
    public ArrayList<Leaf> leafs = new ArrayList<>();

    public Leaf(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void create() {
        final int MAX_LEAF_SIZE = 25;
        Leaf root = new Leaf(0, 0, width - 20, height - 20);
        leafs.add(root);

        boolean didSplit = true;

        while (didSplit) {
            didSplit = false;
            for (int i = 0; i < leafs.size(); i++) {
                Leaf leaf = leafs.get(i);
                if (leaf.leftChild == null && leaf.rightChild == null) {
                    if (leaf.width > MAX_LEAF_SIZE || leaf.height > MAX_LEAF_SIZE || MathUtils.random() >= 1.75) {
                        if (leaf.split()) {
                            leafs.add(leaf.leftChild);
                            leafs.add(leaf.rightChild);
                            didSplit = true;
                        }
                    }
                }
            }

            root.createRooms();
        }

    }

    public void createRooms() {
        if (leftChild != null || rightChild != null) {
            if (leftChild != null) {
                leftChild.createRooms();
            }
            if (rightChild != null) {
                rightChild.createRooms();
            }

            if (leftChild != null && rightChild != null) {
                createHall(leftChild.getRoom(), rightChild.getRoom());
            }

        } else {
            Point roomSize = new Point(MathUtils.random(3, width - 3), MathUtils.random(3, height - 3));
            Point roomPos = new Point(MathUtils.random(1, width - roomSize.x), MathUtils.random(1, height - roomSize.y));

            room = new Rectangle(x + roomPos.x, y + roomPos.y, roomSize.x, roomSize.y);
            rooms.add(room);
            System.out.println("Created new room: " + room.toString());
        }
    }

    public Rectangle getRoom() {
        if (room != null) return room;
        else {
            Rectangle lRoom = null, rRoom = null;

            if (leftChild != null) {
                lRoom = leftChild.getRoom();
            }
            if (rightChild != null) {
                rRoom = rightChild.getRoom();
            }
            if (lRoom == null && rRoom == null) return null;
            else if (rRoom == null) return lRoom;
            else if (lRoom == null) return rRoom;
            else if (MathUtils.random() > 0.5f) return lRoom;
            else return rRoom;
        }
    }

    public ArrayList<Rectangle> getRooms() {
        return this.rooms;
    }

    public ArrayList<Leaf> getLeafs() {
        return this.leafs;
    }

    public void createHall(Rectangle left, Rectangle right) {
        halls = new ArrayList<Rectangle>();

        Vector2 point1 = new Vector2(MathUtils.random(left.x - 1, (left.x + left.width) - 3), MathUtils.random(left.y - 1, (left.y + left.height) - 3));
        Vector2 point2 = new Vector2(MathUtils.random(right.x - 1, (right.x + right.width) - 3), MathUtils.random(right.y - 1, (right.y + right.height) - 3));

        int width = (int) (point2.x - point1.x);
        int height = (int) (point2.y - point1.y);

        if (width < 0) {
            if (height < 0) {
                if (MathUtils.random() < 0.5) {
                    halls.add(new Rectangle(point2.x, point1.y, Math.abs(width), 1));
                    halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(height)));
                } else {
                    halls.add(new Rectangle(point2.x, point2.y, Math.abs(width), 1));
                    halls.add(new Rectangle(point1.x, point2.y, 1, Math.abs(height)));
                }
            } else if (height > 0) {
                if (MathUtils.random() < 0.5) {
                    halls.add(new Rectangle(point2.x, point1.y, Math.abs(width), 1));
                    halls.add(new Rectangle(point2.x, point1.y, 1, Math.abs(height)));
                } else {
                    halls.add(new Rectangle(point2.x, point2.y, Math.abs(width), 1));
                    halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(height)));
                }
            } else if(height == 0) {
                halls.add(new Rectangle(point2.x, point2.y, Math.abs(width), 1));
            }
        } else if (width > 0) {
            if (height < 0) {
                if (MathUtils.random() < 0.5) {
                    halls.add(new Rectangle(point1.x, point2.y, Math.abs(width), 1));
                    halls.add(new Rectangle(point1.x, point2.y, 1, Math.abs(height)));
                } else {
                    halls.add(new Rectangle(point1.x, point1.y, Math.abs(width), 1));
                    halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(height)));
                }
            } else if (height > 0) {
                if (MathUtils.random() < 0.5) {
                    halls.add(new Rectangle(point1.x, point1.y, Math.abs(width), 1));
                    halls.add(new Rectangle(point2.x, point1.y, 1, Math.abs(height)));
                } else {
                    halls.add(new Rectangle(point1.x, point2.y, Math.abs(width), 1));
                    halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(height)));
                }
            } else if (height == 0) {
                halls.add(new Rectangle(point1.x, point1.y, Math.abs(width), 1));
            }
        } else if (width == 0) {
            if (height < 0) {
                halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(height)));
            } else if (height > 0) {
                halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(height)));
            }
        }

    }

    public ArrayList<Rectangle> getHalls() {
        return this.halls;
    }


    public boolean split() {
        if (leftChild != null || rightChild != null) return false;


        // Determine direction of the split
        // if the width is >25% split vertically
        // if the height is >25% split horizontally
        boolean splitH = MathUtils.randomBoolean(0.50f);
        if (width > height && width / height >= 1.25) splitH = false;
        else if (height > width && height / width >= 1.25) splitH = true;

        int max = (splitH ? height : width) - MIN_LEAF_SIZE; // determine max height and width

        if (max <= MIN_LEAF_SIZE) return false; // area is too small to split

        int split = MathUtils.random(MIN_LEAF_SIZE, max);

        // Create left and right children based on split
        if (splitH) {
            leftChild = new Leaf(x, y, width, split);
            rightChild = new Leaf(x, y + split, width, height - split);
        } else {
            leftChild = new Leaf(x, y, split, height);
            rightChild = new Leaf(x + split, y, width - split, height);
        }

        return true;
    }

    private int getWidth() {
        return width;
    }

    private int getHeight() {
        return height;
    }

}
