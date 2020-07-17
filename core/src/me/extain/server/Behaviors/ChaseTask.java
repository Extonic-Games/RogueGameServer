package me.extain.server.Behaviors;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.steer.behaviors.Seek;

import me.extain.server.objects.GameObject;

public class ChaseTask extends LeafTask<GameObject> {


    public void start() {
        super.start();
    }

    @Override
    public Status execute() {

        getObject().setSteeringBehavior(new Seek<>(getObject(), getObject().getTarget()));

        if (getObject().getTarget() == null) getObject().setSteeringBehavior(null);

        return Status.SUCCEEDED;
    }

    @Override
    protected Task<GameObject> copyTo(Task<GameObject> task) {
        return task;
    }

    public void reset() {
        super.reset();
    }
}
