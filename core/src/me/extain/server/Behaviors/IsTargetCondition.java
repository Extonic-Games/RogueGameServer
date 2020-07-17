package me.extain.server.Behaviors;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

import me.extain.server.objects.GameObject;

public class IsTargetCondition extends LeafTask<GameObject> {


    @Override
    public Status execute() {
        return getObject().isTarget() ? Status.SUCCEEDED : Status.FAILED;
    }

    @Override
    protected Task<GameObject> copyTo(Task<GameObject> task) {
        return task;
    }
}
