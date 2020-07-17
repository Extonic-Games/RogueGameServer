package me.extain.server.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import me.extain.server.Physics.Box2DHelper;
import me.extain.server.objects.Player.Player;

public class GameObjectManager {

    private final CopyOnWriteArrayList<GameObject> objects;
    private ArrayList<GameObject> removeObjects;
    private ObjectComparator comparator = new ObjectComparator();

    public GameObjectManager() {
        objects = new CopyOnWriteArrayList<>();
        removeObjects = new ArrayList<>();
    }

    public void update(float deltaTime) {
        objects.sort(comparator);

        for (GameObject object : removeObjects) {
            objects.remove(object);
                Box2DHelper.getWorld().destroyBody(object.getBody());
                if (object.getEyesBody() != null)
                    Box2DHelper.getWorld().destroyBody(object.getEyesBody());
                object.dispose();
            }

        removeObjects.clear();

        for (GameObject object : objects) {
            if (object.isDestroy()) {
                removeObjects.add(object);
                object.clearProjectiles();
            } else {
                synchronized (object) {
                    object.update(deltaTime);
                }
            }
        }

    }

    public void render(SpriteBatch batch) {
    }

    public void addGameObject(GameObject object) {
        synchronized (this.objects) {
            this.objects.add(object);
        }
    }

    public Player getPlayer() {
        for (GameObject object : objects) {
            if (object instanceof Player) {
                return (Player) object;
            }
        }

        return null;
    }

    public void removeObject(String name) {
        for (GameObject object : objects) {
            if (object.getName().equals(name)) {
                removeObjects.add(object);
            }
        }
    }

    public CopyOnWriteArrayList<GameObject> getGameObjects() {
        return objects;
    }

    protected static class ObjectComparator implements Comparator<GameObject> {

        @Override
        public int compare(GameObject gameObject, GameObject t1) {
            return (t1.getPosition().y - gameObject.getPosition().y) > 0 ? 1 : -1;
        }
    }

}
