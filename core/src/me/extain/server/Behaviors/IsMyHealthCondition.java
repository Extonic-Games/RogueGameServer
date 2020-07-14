package me.extain.server.Behaviors;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;

import me.extain.server.GameObject;

public class IsMyHealthCondition extends LeafTask<GameObject> {

    @TaskAttribute(required = true) public float health;

    @Override
    public Status execute() {

        if (getObject().getHealth() <= health) return Status.SUCCEEDED;
        else return Status.FAILED;
    }

    @Override
    protected Task<GameObject> copyTo(Task<GameObject> task) {
        IsMyHealthCondition healthCondition = (IsMyHealthCondition) task;
        healthCondition.health = health;

        return task;
    }
}
