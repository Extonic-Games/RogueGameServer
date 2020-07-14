package me.extain.server.Projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import me.extain.server.Physics.Box2DHelper;

public class TestProjectile extends Projectile {

    public TestProjectile(Vector2 position, Vector2 velocity, short mask) {
        super(position, Box2DHelper.createDynamicBodyCircle(position, 2.5f, mask));

        this.setMinDamage(5);
        this.setMaxDamage(15);
        this.setLifeSpan(10);

        this.setVelocity(velocity);
    }

    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        super.render(batch);
    }


}
