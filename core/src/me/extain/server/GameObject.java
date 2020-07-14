package me.extain.server;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.HashMap;

import me.extain.server.Behaviors.Behaviors;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.Projectile.Projectile;
import me.extain.server.map.tiled.TileMap;
import me.extain.server.packets.LootDropPacket;
import me.extain.server.packets.ShootPacket;
import me.extain.server.packets.UpdatePacket;

public class GameObject implements Steerable<Vector2> {

    private Screen context;
    private TileMap map;
    private Vector2 position;
    private Body body;

    private Vector2 linearVelocity;
    private float orientation;
    private float angularVelocity;
    private float maxLinearSpeed;
    private float maxLinearAcceleration;
    private float maxAngularSpeed;
    private boolean independentFacing;

    private SteeringBehavior<Vector2> steeringBehavior;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

    private int id;

    private float speed;

    private String objectName;

    private TextureAtlas atlas;
    private float health, maxHealth;

    public float shootTimer = 40;

    public boolean isDestroy = false;

    private Body eyesBody;

    public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private ArrayList<Projectile> removeProjectiles = new ArrayList<Projectile>();

    private ArrayList<Behaviors> behaviors = new ArrayList<Behaviors>();

    private BehaviorTree<GameObject> behaviorTree;

    private boolean isBlink = false;

    private int blinkTimer = 20;

    private float stateTime;

    private boolean isMoving, hasMoved;
    private Vector2 oldPos = new Vector2();

    private float alpha = 0.0f;

    private boolean hasEyes = false;

    private float size;

    private GameObject target = null;

    private boolean isTarget = false;

    private HashMap<Integer, String> lootTable;

    private boolean isFlip = false;

    public GameObject(Vector2 position, Body body) {
        this.position = position;
        this.body = body;
        this.speed = 10;
        this.maxHealth = 30;
        this.health = maxHealth;
        this.id = MathUtils.random(1000);

        this.independentFacing = true;
        this.maxLinearSpeed = 2;
        this.maxLinearAcceleration = 2;
        this.maxAngularSpeed = 3;

        this.linearVelocity = new Vector2(body.getLinearVelocity());

        lootTable = new HashMap<>();
        lootTable.put(5, "stick");


        //this.objectName = "GameObject";


        if (body != null)
            this.body.setUserData(this);
    }

    public GameObject(GameObjectWrapper wrapper, Vector2 positon, Body body) {
        this.objectName = wrapper.name;
        this.position = positon;
        this.body = body;
        this.body.setUserData(this);
        this.speed = 10f;
        this.maxHealth = wrapper.health;
        this.health = wrapper.health;
        this.size = wrapper.size;

        this.id = MathUtils.random(1000);

        this.independentFacing = true;
        this.maxLinearSpeed = 2;
        this.maxLinearAcceleration = 2;
        this.maxAngularSpeed = 3;

        this.linearVelocity = new Vector2(body.getLinearVelocity());

        lootTable = new HashMap<>();
        lootTable.put(5, "stick");
    }

    public void update(float deltaTime) {

        alpha += deltaTime;

        stateTime += deltaTime;

        this.getBody().setLinearDamping(5f);

        if (this.getBody().getLinearVelocity().x == 0 && this.getBody().getLinearVelocity().y == 0) {
            isMoving = false;
        }


        for (Projectile projectile : removeProjectiles) {
            Box2DHelper.getWorld().destroyBody(projectile.getBody());
            projectiles.remove(projectile);
        }

        removeProjectiles.clear();

        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, 1f);
        }

        updateBehaviors(deltaTime);
        if (behaviorTree != null)
            behaviorTree.step();

        for (Projectile projectile : projectiles) {

            if (projectile.getDestroy()) {
                removeProjectiles.add(projectile);
            }
            else {
                projectile.update(deltaTime);
            }
        }

        this.position.set(this.getBody().getPosition());
        if (eyesBody != null) eyesBody.setTransform(this.position, 0f);

        if (oldPos.x != this.getPosition().x || oldPos.y != this.getPosition().y) {
            oldPos.set(position);
            hasMoved = true;
        }
    }

    public void render(SpriteBatch batch) {
    }

    public void updateBehaviors(float deltaTime) {
        for (Behaviors behavior : behaviors) {
            behavior.update(deltaTime);
        }
    }

    public void setBehaviorTarget(GameObject object) {

        if (object != null) {
            target = object;
            isTarget = true;
        } else {
            isTarget = false;
        }


        for (Behaviors behavior : behaviors) {
            //behavior.setTarget(object);
        }
    }

    public void clearProjectiles() {
        for (Projectile projectile : projectiles) {
            Box2DHelper.getWorld().destroyBody(projectile.getBody());
        }

        projectiles.clear();
    }

    public void onHit(GameObject object, float damage, int shooterID) {
        if (!(object instanceof Projectile)) {
            object.takeDamage(damage);

            System.out.println(object.getName() + ": health: " + object.getHealth());

            isBlink = true;

            UpdatePacket packet = new UpdatePacket();
            packet.name = objectName;
            packet.id = id;
            packet.x = position.x;
            packet.y = position.y;
            packet.health = health;
            RogueGameServer.getInstance().getServer().sendToAllUDP(packet);

            if (health <= 0) {
                calculateLoot(shooterID);
                this.isDestroy = true;
            }
        }
    }

    private void calculateLoot(int shooterID) {
        float randomValue = MathUtils.random(10);

        String lootChance;

        System.out.println(randomValue);
        lootChance = lootTable.get(MathUtils.random(10));
        lootChance = lootTable.get(MathUtils.random(10));
        lootChance = lootTable.get(MathUtils.random(10));

            // Drop loot bag
            ArrayList<String> items = new ArrayList<>();
            //items.add(lootChance);
            items.add("stick");
            LootDropPacket lootDropPacket = new LootDropPacket();
            lootDropPacket.id = (int) MathUtils.random(5000, 6000);
            lootDropPacket.items = items;
            lootDropPacket.x = this.getPosition().x;
            lootDropPacket.y = this.getPosition().y;

            RogueGameServer.getInstance().getServer().sendToAllUDP(lootDropPacket);
    }

    public void shoot(Projectile projectile) {
        projectiles.add(projectile);

        ShootPacket packet = new ShootPacket();
        packet.name = projectile.getName();
        packet.mask = Box2DHelper.BIT_ENEMYPROJ;
        packet.id = id;
        packet.x = position.x;
        packet.y = position.y;
        packet.velX = projectile.getVelocity().x;
        packet.velY = projectile.getVelocity().y;
        RogueGameServer.getInstance().getServer().sendToAllUDP(packet);
    }

    public void createEyes() {
        eyesBody = Box2DHelper.createSensorCircle(this.position, 80f, Box2DHelper.BIT_ENEMY_SENSOR);
        eyesBody.setUserData(this);
        hasEyes = true;
    }

    public void move(Vector2 velocity) {
        this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x + velocity.x, this.getBody().getLinearVelocity().y + velocity.y);

        UpdatePacket packet = new UpdatePacket();
        packet.name = objectName;
        packet.id = id;
        packet.x = position.x;
        packet.y = position.y;
        packet.health = health;
        RogueGameServer.getInstance().getServer().sendToAllUDP(packet);

        isMoving = true;
    }

    public void applySteering(SteeringAcceleration<Vector2> steering, float time) {
        this.linearVelocity.mulAdd(steering.linear, time).limit(this.getMaxLinearSpeed());

       move(linearVelocity);
       body.applyAngularImpulse(steering.angular, true);


        if (independentFacing) {
            this.orientation += angularVelocity * time;
            this.angularVelocity += steering.angular * time;
        } else {
            float newOrientation = calculateOrientationFromLinearVelocity(this);
            this.angularVelocity = (newOrientation - this.orientation) * time;
            this.orientation = newOrientation;
        }
    }

    public static float calculateOrientationFromLinearVelocity(Steerable<Vector2> object) {
        if (object.getLinearVelocity().isZero(MathUtils.FLOAT_ROUNDING_ERROR))
            return object.getOrientation();

        return object.vectorToAngle(object.getLinearVelocity());
    }

    public void addBehavior(Behaviors behavior) {
        this.behaviors.add(behavior);
    }

    public void setBehaviorTree(BehaviorTree<GameObject> btree) {
        this.behaviorTree = btree;
    }

    public BehaviorTree<GameObject> getBehaviorTree() {
        return behaviorTree;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> behavior) {
        this.steeringBehavior = behavior;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void removeBehavior(Behaviors behavior) { this.behaviors.remove(behavior); }

    public ArrayList<Behaviors> getBehaviors() {
        return behaviors;
    }

    public boolean isDestroy() {
        return this.isDestroy;
    }

    public float getHealth() {
        return health;
    }

    public GameObject getTarget() {
        return target;
    }

    public float getMaxHealth() { return maxHealth; }

    public void takeDamage(float damage) {
        health -= damage;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public TileMap getMap() {
        return map;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {

        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);

        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return (Location<Vector2>) this.position;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setMaxHealth(float maxHealth) { this.maxHealth = maxHealth; }

    public void setAtlas(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getName() {
        return objectName;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public float getShootTimer() {
        return shootTimer;
    }

    public void setShootSpeed(float shootSpeed) {
        this.shootTimer = shootSpeed;
    }

    public Body getEyesBody() {
        return eyesBody;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public boolean isHasEyes() {
        return hasEyes;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return this.linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return this.angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return 0;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {

    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return this.maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return this.maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

    }

    public void dispose() {
        atlas.dispose();
    }

    protected boolean isFlip() {
        return isFlip;
    }
}
