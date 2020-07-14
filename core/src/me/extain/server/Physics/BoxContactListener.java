package me.extain.server.Physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import me.extain.server.GameObject;
import me.extain.server.Player.Player;
import me.extain.server.Projectile.Projectile;
import me.extain.server.item.Item;
import me.extain.server.map.tiled.TileMapHelper;

public class BoxContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (bodyB.getUserData() instanceof Projectile && bodyA.getUserData() instanceof GameObject) {
            GameObject object = (GameObject) bodyA.getUserData();
            Projectile projectile = (Projectile) bodyB.getUserData();

            if (!(object instanceof Projectile)) {
                projectile.setDestroy(true);
                object.onHit(object, projectile.getDamageRange(), projectile.shooterID);
            }
        } else if (bodyA.getUserData() instanceof Projectile && bodyB.getUserData() instanceof GameObject) {
            GameObject object = (GameObject) bodyB.getUserData();
            Projectile projectile = (Projectile) bodyA.getUserData();

            if (!(object instanceof Projectile)) {
                projectile.setDestroy(true);
                object.onHit(object, projectile.getDamageRange(), projectile.shooterID);
            }
        }

        if (bodyB.getUserData() instanceof Projectile && bodyA.getUserData() instanceof TileMapHelper) {
            Projectile projectile = (Projectile) bodyB.getUserData();

            if (bodyA.getUserData() != null) {
                projectile.setDestroy(true);
            }
        } else if (bodyA.getUserData() instanceof Projectile && bodyB.getUserData() instanceof TileMapHelper) {
            Projectile projectile = (Projectile) bodyA.getUserData();

            if (bodyA.getUserData() != null) {
                projectile.setDestroy(true);
            }
        }

        if (bodyA.getUserData() instanceof Player && fixtureB.getFilterData().categoryBits == Box2DHelper.BIT_ENEMY_SENSOR) {
            Player player = (Player) bodyA.getUserData();
            GameObject object = (GameObject) bodyB.getUserData();

            object.setBehaviorTarget(player);
            System.out.println(object.getID() + " has found " + player.getID());
        } else if (bodyB.getUserData() instanceof Player && fixtureA.getFilterData().categoryBits == Box2DHelper.BIT_ENEMY_SENSOR) {
            Player player = (Player) bodyB.getUserData();
            GameObject object = (GameObject) bodyA.getUserData();

            object.setBehaviorTarget(player);
            System.out.println(object.getID() + " has found " + player.getID());
        }

        if (bodyA.getUserData() instanceof Player && fixtureB.getFilterData().categoryBits == Box2DHelper.BIT_ITEM) {
            Player player = (Player) bodyA.getUserData();
            Item item = (Item) bodyB.getUserData();


            item.destroyBody();
        }
        else if (bodyB.getUserData() instanceof Player && fixtureA.getFilterData().categoryBits == Box2DHelper.BIT_ITEM) {
            Player player = (Player) bodyB.getUserData();
            Item item = (Item) bodyA.getUserData();


            item.destroyBody();
        }

    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();


        if (bodyA.getUserData() instanceof Player && fixtureB.getFilterData().categoryBits == Box2DHelper.BIT_ENEMY_SENSOR) {
            GameObject object = (GameObject) bodyB.getUserData();

            object.setBehaviorTarget(null);
        } else if (bodyB.getUserData() instanceof Player && fixtureA.getFilterData().categoryBits == Box2DHelper.BIT_ENEMY_SENSOR) {
            GameObject object = (GameObject) bodyA.getUserData();

            object.setBehaviorTarget(null);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        short bitA = fixtureA.getFilterData().maskBits;
        short bitB = fixtureB.getFilterData().maskBits;

        short colA = fixtureA.getFilterData().categoryBits;
        short colB = fixtureB.getFilterData().categoryBits;

        boolean collide = (bitA & colB) != 0 || (colA & bitB) != 0;

        contact.setEnabled(collide);

        //contact.setEnabled(true);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
