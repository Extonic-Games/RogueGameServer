package me.extain.server.Behaviors;

import me.extain.server.GameObject;

public class TransitionBehavior {

    private Behaviors oldState, changeState;
    private GameObject object;

    public TransitionBehavior(GameObject object, Behaviors oldState, Behaviors changeState) {
        this.changeState = changeState;
        this.oldState = oldState;
        this.object = object;
    }

    public void transitionOnHealth(float changeOnHealthPercent) {
        if (object.getHealth() >= changeOnHealthPercent) {
            object.addBehavior(changeState);
            object.removeBehavior(oldState);
        }
    }

}
