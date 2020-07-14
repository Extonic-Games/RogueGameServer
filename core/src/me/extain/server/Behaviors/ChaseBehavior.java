package me.extain.server.Behaviors;

import com.badlogic.gdx.math.Vector2;

import me.extain.server.GameObject;
import me.extain.server.RogueGameServer;
import me.extain.server.packets.UpdatePacket;

public class ChaseBehavior implements Behaviors {

    public GameObject object, target;
    public Vector2 velocity;

    public ChaseBehavior(GameObject object) {
        this.object = object;

        velocity = new Vector2();
    }

    @Override
    public void update(float deltaTime) {
        if (target != null) {
            velocity.x = (target.getPosition().x - object.getPosition().x + object.getSpeed()) * deltaTime;
            velocity.y = (target.getPosition().y - object.getPosition().y + object.getSpeed()) * deltaTime;

            object.move(velocity);


            //System.out.println("Sent object move packet");
        }

    }

    @Override
    public void setTarget(GameObject object) {
        this.target = object;
    }

    public GameObject getObject() {
        return this.object;
    }
}
