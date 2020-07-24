package me.extain.server.Projectile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import me.extain.server.objects.GameObject;

public class SwordSlash extends Projectile {

    public SwordSlash(Vector2 pos, Body body) {
        super(pos, body);

        this.getBody().setUserData(this);
    }

    public void update(float deltaTime) {
        float lifeSpan = getLifeSpan();

        if (lifeSpan != 0)  setLifeSpan(lifeSpan--);

        if (lifeSpan == 0) setDestroy(true);

        float angle = MathUtils.atan2(this.getVelocity().y, this.getVelocity().x) * MathUtils.radiansToDegrees;

        this.getPosition().set(this.getBody().getPosition());

        this.getBody().setTransform(this.getPosition(), angle * MathUtils.degreesToRadians);
    }
}
