package me.extain.server.Behaviors;


import com.badlogic.gdx.math.Vector2;

import me.extain.server.GameObject;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.Projectile.ProjectileFactory;
import me.extain.server.RogueGameServer;
import me.extain.server.packets.ShootPacket;


public class ShootBehavior implements Behaviors {

    public GameObject object, target;
    public float shootTimer;
    private String projectile;

    public ShootBehavior(GameObject object) {
        this.object = object;
        this.shootTimer = object.getShootTimer();
    }

    @Override
    public void update(float deltaTime) {

        if (shootTimer != 0) shootTimer--;

        if (shootTimer == 0 && target != null) {
            float playerX = target.getPosition().x;
            float playerY = target.getPosition().y;

            float targetX = playerX;
            float targetY = playerY;

            float dirLength = (float) Math.sqrt((targetX - object.getPosition().x) * (targetX - object.getPosition().x) + (targetY - object.getPosition().y) * (targetY - object.getPosition().y));

            float dirX = (targetX - object.getPosition().x) / dirLength * 40;
            float dirY = (targetY - object.getPosition().y) / dirLength * 40;

            //object.shoot(ProjectileFactory.getInstance().getProjectile(projectile, new Vector2(object.getPosition().x, object.getPosition().y), new Vector2(dirX, dirY), Box2DHelper.BIT_ENEMYPROJ));
            ShootPacket packet = new ShootPacket();
            packet.name = projectile;
            packet.mask = Box2DHelper.BIT_ENEMYPROJ;
            packet.id = object.getID();
            packet.x = object.getPosition().x;
            packet.y = object.getPosition().y;
            packet.velX = dirX;
            packet.velY = dirY;
            RogueGameServer.getInstance().getServer().sendToAllUDP(packet);
            shootTimer = object.getShootTimer();
        }
    }

    public void setProjectile(String projectile) {
        this.projectile = projectile;
    }

    @Override
    public void setTarget(GameObject object) {
        this.target = object;
    }
}
