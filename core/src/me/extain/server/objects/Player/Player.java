package me.extain.server.objects.Player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import me.extain.server.Projectile.Projectile;
import me.extain.server.Projectile.ProjectileFactory;
import me.extain.server.Projectile.SwordSlash;
import me.extain.server.objects.GameObject;
import me.extain.server.Physics.Box2DHelper;
import me.extain.server.RogueGameServer;
import me.extain.server.item.Item;
import me.extain.server.network.packets.PlayerStatsPacket;
import me.extain.server.network.packets.ShootPacket;
import me.extain.server.network.packets.UpdatePacket;

public class Player extends GameObject {
    private float shootTimer = 20;

    private Vector2 oldPos;

    private String username;

    private boolean canShoot = false;

    private PlayerStats playerStats;

    public Player(Vector2 position) {
        super(position, Box2DHelper.createDynamicBodyCircle(position, 4f, Box2DHelper.BIT_PLAYER));

        this.setObjectName("Player");

        this.getBody().setUserData(this);

        this.setSpeed(30);

        playerStats = new PlayerStats();

        oldPos = new Vector2(0,0);

    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        this.getBody().setLinearDamping(5);

        if (shootTimer != 0) {
            shootTimer--;
        }
        else {
            canShoot = true;
        }

        this.getPosition().set(this.getBody().getPosition());

        UpdatePacket packet = new UpdatePacket();
        packet.name = this.getName();
        packet.id = this.getID();
        packet.x = getPosition().x;
        packet.y = getPosition().y;
        packet.health = this.getHealth();
        RogueGameServer.getInstance().getServer().sendToAllUDP(packet);

        if (playerStats.getXp() > playerStats.getXpNeeded())
            playerStats.calculateLevel();

        if (playerStats.getLastLevel() < playerStats.getLevel()) {
            playerStats.setLastLevel(playerStats.getLevel());

            PlayerStatsPacket playerStatsPacket = new PlayerStatsPacket();
            playerStatsPacket.level = playerStats.getLevel();
            playerStatsPacket.xp = playerStats.getXp();
            playerStatsPacket.attack = playerStats.getAttack();

            RogueGameServer.getInstance().getServer().sendToUDP(this.getID(), playerStatsPacket);
        }
    }

    public ShootPacket shoot(ShootPacket packet, Item item) {
        canShoot = false;
        shootTimer = 20 * item.getWeaponStats().getAttackSpeed();

        if (item.isSword()) {
            SwordSlash slash = ProjectileFactory.getInstance().getSlash(item.getWeaponStats().getProjectile(), new Vector2(packet.x, packet.y), new Vector2(packet.velX, packet.velY), Box2DHelper.BIT_PROJECTILES);
            slash.setMaxDamage(item.getWeaponStats().getMaxDamage());
            slash.setMinDamage(item.getWeaponStats().getDamage());
            slash.setShooterID(packet.id);
            packet.name = item.getWeaponStats().getProjectile();
            packet.mask = Box2DHelper.BIT_PROJECTILES;
            packet.damage = slash.getDamageRange();
            packet.lifeSpan = item.getWeaponStats().getLifeSpan();
            packet.isSlash = true;

            if (!Box2DHelper.getWorld().isLocked())
                RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects().add(slash);
        } else {
            Projectile projectile = ProjectileFactory.getInstance().getProjectile(item.getWeaponStats().getProjectile(), new Vector2(packet.x, packet.y), new Vector2(packet.velX, packet.velY), Box2DHelper.BIT_PROJECTILES);
            projectile.setMinDamage(item.getWeaponStats().getDamage());
            projectile.setMaxDamage(item.getWeaponStats().getMaxDamage());
            projectile.setShooterID(packet.id);
            packet.name = item.getWeaponStats().getProjectile();
            packet.mask = Box2DHelper.BIT_PROJECTILES;
            packet.damage = projectile.getDamageRange();
            packet.lifeSpan = item.getWeaponStats().getLifeSpan();

            packet.isSlash = false;

            if (!Box2DHelper.getWorld().isLocked())
                RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects().add(projectile);
        }

        return packet;
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

    public boolean isCanShoot() {
        return canShoot;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }
}
