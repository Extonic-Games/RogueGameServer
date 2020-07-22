package me.extain.server.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Box2DHelper {

    private static World world;
    //private Box2DDebugRenderer debugRenderer;

    public static final short BIT_PLAYER = 1;
    public static final short BIT_ENEMY = 2;
    public static final short BIT_OBJECTS = 4;
    public static final short BIT_PROJECTILES = 8;
    public static final short BIT_ENEMYPROJ = 16;
    public static final short BIT_ENEMY_SENSOR = 32;
    public static final short BIT_ITEM = 64;

    public static final short MASK_PLAYER = BIT_OBJECTS | BIT_ENEMYPROJ | BIT_ENEMY_SENSOR | BIT_ITEM;
    public static final short MASK_ENEMY = BIT_OBJECTS | BIT_PROJECTILES;
    public static final short MASK_PROJECTILES = BIT_OBJECTS | BIT_ENEMY;
    public static final short MASK_ENEMYPROJ = BIT_OBJECTS | BIT_PLAYER;
    public static final short MASK_ENEMY_SENSOR = BIT_PLAYER;
    public static final short MASK_ITEM = BIT_PLAYER;
    public static final short MASK_OBJECTS = -1;

    //public static final short GROUP_PLAYER = -1;
    //public static final short GROUP_ENEMY = -2;
    //public static final short GROUP_OBJECTS = 1;

    private BoxContactListener boxContactListener = new BoxContactListener();

    private static ArrayList<Body> bodiesToDestroy = new ArrayList<Body>();

    public void createWorld() {
        world = new World(new Vector2(0,0), true);
        //debugRenderer = new Box2DDebugRenderer();

        world.setContactListener(boxContactListener);
    }

    public void render(OrthographicCamera camera) {
        //debugRenderer.render(this.world, camera.combined);
    }

    public void step() {
        world.step(1/60f, 6, 2);
    }

    public static Body createDynamicBodyCircle(Vector2 pos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);

        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(4f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0f;

        fixtureDef.filter.categoryBits = BIT_OBJECTS;
        fixtureDef.filter.maskBits = BIT_OBJECTS;
        //fixtureDef.filter.groupIndex = GROUP_OBJECTS;

        Fixture fixture = body.createFixture(fixtureDef);
        circle.dispose();

        return body;
    }

    public static Body createSensorCircle(Vector2 pos, float radius, short mask) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);

        Body body = null;

        if (world.isLocked()) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    world.createBody(bodyDef);
                }
            });
        } else {
            body = world.createBody(bodyDef);
        }

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0f;
        fixtureDef.isSensor = true;

        if (mask == BIT_ENEMY_SENSOR) {
            fixtureDef.filter.categoryBits = BIT_ENEMY_SENSOR;
            fixtureDef.filter.maskBits = MASK_ENEMY_SENSOR;
        }

         else if (mask == BIT_ITEM) {
            fixtureDef.filter.categoryBits = BIT_ITEM;
            fixtureDef.filter.maskBits = MASK_ITEM;
        }
        //fixtureDef.filter.groupIndex = GROUP_OBJECTS;

        Fixture fixture = body.createFixture(fixtureDef);
        circle.dispose();

        return body;
    }

    public static Body createDynamicBodyCircle(Vector2 pos, float radius, short mask) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);

        Body body = null;

        if (world.isLocked()) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    world.createBody(bodyDef);
                }
            });
        } else {
            body = world.createBody(bodyDef);
        }


        if (body != null) {
            CircleShape circle = new CircleShape();
            circle.setRadius(radius);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.restitution = 0f;

            if (mask == BIT_PLAYER) {
                fixtureDef.filter.categoryBits = BIT_PLAYER;
                fixtureDef.filter.maskBits = MASK_PLAYER;
                //fixtureDef.filter.groupIndex = GROUP_PLAYER;
            } else if (mask == BIT_ENEMY) {
                fixtureDef.filter.categoryBits = BIT_ENEMY;
                fixtureDef.filter.maskBits = MASK_ENEMY;
                //fixtureDef.filter.groupIndex = GROUP_ENEMY;
            } else if (mask == BIT_PROJECTILES) {
                fixtureDef.filter.categoryBits = BIT_PROJECTILES;
                fixtureDef.filter.maskBits = MASK_PROJECTILES;
            } else if (mask == BIT_ENEMYPROJ) {
                fixtureDef.filter.categoryBits = BIT_ENEMYPROJ;
                fixtureDef.filter.maskBits = MASK_ENEMYPROJ;
            }


            Fixture fixture = body.createFixture(fixtureDef);
            circle.dispose();
        }

        return body;
    }

    public static Body createDynamicBodyRect(float hx, float hy, Vector2 pos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);

        Body body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(hx, hy);
        body.createFixture(box, 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 1f;

        fixtureDef.filter.categoryBits = BIT_OBJECTS;
        fixtureDef.filter.maskBits = MASK_OBJECTS;
        //fixtureDef.filter.groupIndex = GROUP_OBJECTS;

        Fixture fixture = body.createFixture(fixtureDef);

        box.dispose();

        return body;
    }


    public static Body createDynamicBodyRect(float hx, float hy, Vector2 pos, short mask) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);

        Body body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(hx, hy);
        //body.createFixture(box, 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 1f;

        if (mask == BIT_PLAYER) {
            fixtureDef.filter.categoryBits = BIT_PLAYER;
            fixtureDef.filter.maskBits = MASK_PLAYER;
            //fixtureDef.filter.groupIndex = GROUP_PLAYER;
        }
        else if (mask == BIT_ENEMY) {
            fixtureDef.filter.categoryBits = BIT_ENEMY;
            fixtureDef.filter.maskBits = MASK_ENEMY;
            //fixtureDef.filter.groupIndex = GROUP_ENEMY;
        }
        else if (mask == BIT_PROJECTILES) {
            fixtureDef.filter.categoryBits = BIT_PROJECTILES;
            fixtureDef.filter.maskBits = MASK_PROJECTILES;
        }
        else if (mask == BIT_ENEMYPROJ) {
            fixtureDef.filter.categoryBits = BIT_ENEMYPROJ;
            fixtureDef.filter.maskBits = MASK_ENEMYPROJ;
        }

        Fixture fixture = body.createFixture(fixtureDef);

        box.dispose();

        return body;
    }

    public static Body createSensorBodyRect(float hx, float hy, Vector2 pos, short mask) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);

        Body body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(hx, hy);
        //body.createFixture(box, 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 1f;
        fixtureDef.isSensor = true;

        if (mask == BIT_PLAYER) {
            fixtureDef.filter.categoryBits = BIT_PLAYER;
            fixtureDef.filter.maskBits = MASK_PLAYER;
            //fixtureDef.filter.groupIndex = GROUP_PLAYER;
        }
        else if (mask == BIT_ENEMY) {
            fixtureDef.filter.categoryBits = BIT_ENEMY;
            fixtureDef.filter.maskBits = MASK_ENEMY;
            //fixtureDef.filter.groupIndex = GROUP_ENEMY;
        }
        else if (mask == BIT_PROJECTILES) {
            fixtureDef.filter.categoryBits = BIT_PROJECTILES;
            fixtureDef.filter.maskBits = MASK_PROJECTILES;
        }
        else if (mask == BIT_ENEMYPROJ) {
            fixtureDef.filter.categoryBits = BIT_ENEMYPROJ;
            fixtureDef.filter.maskBits = MASK_ENEMYPROJ;
        }

        Fixture fixture = body.createFixture(fixtureDef);

        box.dispose();

        return body;
    }

    public static Body createStaticBody(float hx, float hy, Vector2 pos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pos);

        Body body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(hx, hy);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 1f;

        fixtureDef.filter.categoryBits = BIT_OBJECTS;
        fixtureDef.filter.maskBits = MASK_OBJECTS;
        //fixtureDef.filter.groupIndex = GROUP_OBJECTS;

        Fixture fixture = body.createFixture(fixtureDef);


        box.dispose();

        return body;
    }

    public static void setBodyToDestroy(Body body) {
        bodiesToDestroy.add(body);
    }

    public static World getWorld() {
        return world;
    }

}
