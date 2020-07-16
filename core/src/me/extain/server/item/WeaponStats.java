package me.extain.server.item;

public class WeaponStats {

    public float damage;
    public float maxDamage;
    public float attackSpeed;
    public String projectile;

    public WeaponStats() {
        super();
    }

    public WeaponStats(float damage, float maxDamage, float attackSpeed, String projectile) {
        this.damage = damage;
        this.maxDamage = maxDamage;
        this.attackSpeed = attackSpeed;
        this.projectile = projectile;
    }

    public float getMaxDamage() {
        return maxDamage;
    }

    public float getDamage() {
        return damage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public String getProjectile() {
        return projectile;
    }

    public void setMaxDamage(float maxDamage) {
        this.maxDamage = maxDamage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setProjectile(String projectile) {
        this.projectile = projectile;
    }
}
