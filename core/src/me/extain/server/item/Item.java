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

    public enum ItemUseType {
        ITEM_RESTORE_HEALTH(1),
        ITEM_RESTORE_MP(2),
        ITEM_DAMAGE(4),
        WEAPON(8),
        ARMOR_HELMET(16),
        ARMOR_CHEST(32),
        ARMOR_FEET(64),
        QUEST_ITEM(128);

        private int itemUseType;

        ItemUseType(int itemUseType) {
            this.itemUseType = itemUseType;
        }

        public int getValue() {
            return itemUseType;
        }
    }

    private int itemAttributes;
    private int itemUseType;
    private int itemUseTypeValue;
    private String itemTypeID;
    private String itemShortDesc;
    private int itemValue;

    private Body body;

    private String name;

    private float scale = 1;

    public Item(TextureRegion textureRegion, int itemAttributes, String itemTypeID, int itemUseType, int itemUseTypeValue, int itemValue) {
        super(textureRegion);

        this.itemTypeID = itemTypeID;
        this.itemAttributes = itemAttributes;
        this.itemUseType = itemUseType;
        this.itemUseTypeValue = itemUseTypeValue;
        this.itemValue = itemValue;
    }

    public Item() {
        super();
    }

    public Item(Item item) {
        super();

        this.itemTypeID = item.getItemTypeID();
        this.itemAttributes = item.getItemAttributes();
        this.itemUseType = item.getItemUseType();
        this.itemUseTypeValue = item.getItemUseTypeValue();
        this.itemShortDesc = item.getItemShortDesc();
        this.itemValue = item.getItemValue();
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

    public void destroyBody() {
        Box2DHelper.setBodyToDestroy(body);
        body = null;
    }

}
