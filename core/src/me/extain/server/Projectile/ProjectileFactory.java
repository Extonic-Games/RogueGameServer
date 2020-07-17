package me.extain.server.Projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Hashtable;

import me.extain.server.Physics.Box2DHelper;

public class ProjectileFactory {

    private Hashtable<String, ProjectileWrapper> projectiles;

    private final String ProjectileScript = "projectiles/projectiles.json";
    private static ProjectileFactory instance = null;

    public static ProjectileFactory getInstance() {
        if (instance == null) instance = new ProjectileFactory();

        return instance;
    }

    public ProjectileFactory() {
        Json json = new Json();
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(ProjectileScript));
        projectiles = new Hashtable<>();

        for (JsonValue jsonValue : list) {
            ProjectileWrapper wrapper = json.readValue(ProjectileWrapper.class, jsonValue);

            projectiles.put(wrapper.name, wrapper);
        }
    }

    public Projectile getProjectile(String name, Vector2 position, Vector2 velocity, short mask) {
        ProjectileWrapper wrapper = projectiles.get(name);

        Projectile projectile = new Projectile(position, Box2DHelper.createDynamicBodyCircle(position, 2.5f, mask));
        projectile.setObjectName(wrapper.name);
        projectile.setMinDamage(wrapper.damage);
        projectile.setMaxDamage(wrapper.maxDamage);
        projectile.setLifeSpan(wrapper.lifespan);
        projectile.setVelocity(velocity);

        return projectile;
    }



}
