package me.extain.server.Behaviors;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.math.Vector2;

import me.extain.server.objects.GameObject;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.Projectile.ProjectileFactory;

public class CircleShotTask extends LeafTask<GameObject> {

    @TaskAttribute(required = true) public String projectile;
    @TaskAttribute(required = true) public int shotSpeed;
    @TaskAttribute(required = true) public float shootTimer;


    @Override
    public Status execute() {
        if (shootTimer != 0) shootTimer--;

        float playerX = getObject().getTarget().getPosition().x;
        float playerY = getObject().getTarget().getPosition().y;

        float dirLength = (float) Math.sqrt((playerX - getObject().getPosition().x)
                * (playerX - getObject().getPosition().x)
                + (playerY - getObject().getPosition().y)
                * (playerY - getObject().getPosition().y));

        float dirX = (playerX - getObject().getPosition().x) / dirLength * shotSpeed;
        float dirY = (playerY - getObject().getPosition().y) / dirLength * shotSpeed;


        if (shootTimer == 0) {
            getObject().shoot(ProjectileFactory.getInstance().getProjectile(projectile,
                    new Vector2(getObject().getPosition().x, getObject().getPosition().y),
                    new Vector2(0, shotSpeed), Box2DHelper.BIT_ENEMYPROJ));

            getObject().shoot(ProjectileFactory.getInstance().getProjectile(projectile,
                    new Vector2(getObject().getPosition().x, getObject().getPosition().y),
                    new Vector2(shotSpeed, 0), Box2DHelper.BIT_ENEMYPROJ));

            getObject().shoot(ProjectileFactory.getInstance().getProjectile(projectile,
                    new Vector2(getObject().getPosition().x, getObject().getPosition().y),
                    new Vector2(-shotSpeed, 0), Box2DHelper.BIT_ENEMYPROJ));

            getObject().shoot(ProjectileFactory.getInstance().getProjectile(projectile,
                    new Vector2(getObject().getPosition().x, getObject().getPosition().y),
                    new Vector2(0, -shotSpeed), Box2DHelper.BIT_ENEMYPROJ));

            shootTimer = getObject().getShootTimer();
        }

        return Status.SUCCEEDED;
    }

    @Override
    protected Task<GameObject> copyTo(Task<GameObject> task) {
        CircleShotTask circleShotTask = (CircleShotTask) task;
        circleShotTask.projectile = projectile;
        circleShotTask.shotSpeed = shotSpeed;
        circleShotTask.shootTimer = shootTimer;


        return task;
    }
}
