package me.extain.server.Behaviors;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.math.Vector2;

import me.extain.server.objects.GameObject;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.Projectile.ProjectileFactory;

public class ShootTask extends LeafTask<GameObject> {

    @TaskAttribute(required = true) public String projectile;
    @TaskAttribute(required = true) public int shotSpeed;
    @TaskAttribute(required = true) public float shootTimer;

    private float maxShootTimer;

    public void start() {
        super.start();

        maxShootTimer = shootTimer;
    }

    @Override
    public Status execute() {

        if (shootTimer != 0) shootTimer--;

        float playerX = getObject().getTarget().getPosition().x;
        float playerY = getObject().getTarget().getPosition().y;

        float targetX = playerX;
        float targetY = playerY;

        float dirLength = (float) Math.sqrt((targetX - getObject().getPosition().x) * (targetX - getObject().getPosition().x) + (targetY - getObject().getPosition().y) * (targetY - getObject().getPosition().y));

        float dirX = (targetX - getObject().getPosition().x) / dirLength * shotSpeed;
        float dirY = (targetY - getObject().getPosition().y) / dirLength * shotSpeed;


        if (shootTimer == 0) {
            getObject().shoot(ProjectileFactory.getInstance().getProjectile(projectile, new Vector2(getObject().getPosition().x, getObject().getPosition().y), new Vector2(dirX, dirY), Box2DHelper.BIT_ENEMYPROJ));
            shootTimer = getObject().getShootTimer();
        }

        return Status.SUCCEEDED;
    }

    @Override
    protected Task<GameObject> copyTo(Task<GameObject> task) {
        ShootTask shoot = (ShootTask)task;
        shoot.projectile = projectile;
        shoot.shotSpeed = shotSpeed;
        shoot.shootTimer = shootTimer;

        return task;
    }
}
