package me.extain.server.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.extain.server.Physics.Box2DHelper;

public class Item extends Image {

    public enum ItemAttribute{
        CONSUMABLE(1),
        EQUIPPABLE(2);

        private int attribute;

        ItemAttribute(int attribute) {
            this.attribute = attribute;
        }

        public int getValue() {
            return attribute;
        }
    }

    public enum WeaponAttribute {
        NONE(1),
        SWORD(2),
        STAFF(4),
        BOW(8);

        private int attribute;

        WeaponAttribute(int attribute) {
            this.attribute = attribute;
        }

        public int getValue() {
            return attribute;
        }
    }

    public enum ItemUseType {
        ITEM_RESTORE_HEALTH(1),
        ITEM_RESTORE_MP(2),
        ITEM_DAMAGE(4),
        WEAPON(8),
        ABILITY(16),
        ARMOR_HELMET(32),
        ARMOR_CHEST(64),
        ARMOR_FEET(128),
        QUEST_ITEM(256);

        private int itemUseType;

        ItemUseType(int itemUseType) {
            this.itemUseType = itemUseType;
        }

        public int getValue() {
            return itemUseType;
        }
    }

    private int itemAttributes;
    private int weaponAttributes;
    private int itemUseType;
    private int itemUseTypeValue;
    private String itemTypeID;
    private String itemShortDesc;
    private int itemValue;
    private WeaponStats weaponStats;

    private Body body;

    private String name;

    private float scale = 1;


    public Item(TextureRegion textureRegion, int itemAttributes, int weaponAttributes, String itemTypeID, int itemUseType, int itemUseTypeValue, int itemValue, WeaponStats weaponStats) {
        super(textureRegion);

        this.itemTypeID = itemTypeID;
        this.itemAttributes = itemAttributes;
        this.weaponAttributes = weaponAttributes;
        this.itemUseType = itemUseType;
        this.itemUseTypeValue = itemUseTypeValue;
        this.itemValue = itemValue;
        this.weaponStats = weaponStats;
    }

    public Item() {
        super();
    }

    public Item(Item item) {
        super();

        this.itemTypeID = item.getItemTypeID();
        this.itemAttributes = item.getItemAttributes();
        this.weaponAttributes = item.getWeaponAttributes();
        this.itemUseType = item.getItemUseType();
        this.itemUseTypeValue = item.getItemUseTypeValue();
        this.itemShortDesc = item.getItemShortDesc();
        this.itemValue = item.getItemValue();
        this.weaponStats = item.getWeaponStats();
    }

    public int getItemUseTypeValue() {
        return itemUseTypeValue;
    }

    public void setItemUseTypeValue(int itemUseTypeValue) {
        this.itemUseTypeValue = itemUseTypeValue;
    }

    public int getItemValue() {
        return itemValue;
    }

    public void setItemValue(int itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(String itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public int getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(int itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public int getWeaponAttributes() {
        return weaponAttributes;
    }

    public void setWeaponAttributes(int weaponAttributes) {
        this.weaponAttributes = weaponAttributes;
    }

    public int getItemUseType() {
        return itemUseType;
    }

    public void setItemUseType(int itemUseType) {
        this.itemUseType = itemUseType;
    }

    public String getItemShortDesc() {
        return itemShortDesc;
    }

    public void setItemShortDesc(String itemShortDesc) {
        this.itemShortDesc = itemShortDesc;
    }

    public boolean isConsumable() {
        return ((itemAttributes & ItemAttribute.CONSUMABLE.getValue()) == ItemAttribute.CONSUMABLE.getValue());
    }

    public boolean isSameItemType(Item item) {
        return itemTypeID == item.getItemTypeID();
    }

    public static boolean doesRestoreHP(int itemUseType) {
        return ((itemUseType & ItemUseType.ITEM_RESTORE_HEALTH.getValue()) == ItemUseType.ITEM_RESTORE_HEALTH.getValue());
    }

    public static boolean doesRestoreMP(int itemUseType) {
        return ((itemUseType & ItemUseType.ITEM_RESTORE_MP.getValue()) == ItemUseType.ITEM_RESTORE_MP.getValue());
    }

    public boolean isEquippable() {
        return ((itemAttributes & ItemAttribute.EQUIPPABLE.getValue()) == ItemAttribute.EQUIPPABLE.getValue());
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isSword() {
        return ((weaponAttributes & WeaponAttribute.SWORD.getValue()) == WeaponAttribute.SWORD.getValue());
    }

    public boolean isStaff() {
        return ((weaponAttributes & WeaponAttribute.STAFF.getValue()) == WeaponAttribute.STAFF.getValue());
    }

    public boolean isBow() {
        return ((weaponAttributes & WeaponAttribute.BOW.getValue()) == WeaponAttribute.BOW.getValue());
    }

    public void setWeaponStats(WeaponStats weaponStats) {
        this.weaponStats = weaponStats;
    }

    public WeaponStats getWeaponStats() {
        return weaponStats;
    }

    public void destroyBody() {
        Box2DHelper.setBodyToDestroy(body);
        body = null;
    }

}
