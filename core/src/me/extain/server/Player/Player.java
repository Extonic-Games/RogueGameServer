package me.extain.server.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import Utils.ConsoleLog;
import me.extain.server.GameObject;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.RogueGameServer;
import me.extain.server.packets.UpdatePacket;

public class Player extends GameObject {
    private float shootTimer = 20;

    private Vector2 oldPos;

    private String username;

    public Player(Vector2 position) {
        super(position, Box2DHelper.createDynamicBodyCircle(position, 4f, Box2DHelper.BIT_PLAYER));

        this.setObjectName("Player");

        this.getBody().setUserData(this);

        this.setSpeed(30);

        oldPos = new Vector2(0,0);

    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        this.getBody().setLinearDamping(5);

        if (shootTimer != 0) shootTimer--;

        this.getPosition().set(this.getBody().getPosition());

        UpdatePacket packet = new UpdatePacket();
        packet.name = this.getName();
        packet.id = this.getID();
        packet.x = getPosition().x;
        packet.y = getPosition().y;
        packet.health = this.getHealth();
        RogueGameServer.getInstance().getServer().sendToAllUDP(packet);
    }



    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    public void setPosition(float x, float y) {
        this.getBody().getPosition().set(x, y);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
