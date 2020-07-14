package me.extain.server.Behaviors;

import me.extain.server.GameObject;

public interface Behaviors {

    void update(float deltaTime);

    void setTarget(GameObject object);

}
