package com.aesophor.medievania.component;

import com.aesophor.medievania.entity.item.Item;
import com.badlogic.ashley.core.Component;

public class EquipmentComponent implements Component {

    private Item headGear;
    private Item armor;
    private Item gauntlets;
    private Item boots;
    private Item cape;

    private Item leftHandedWeapon;
    private Item rightHandedWeapon;

    public EquipmentComponent() {

    }


    public Item getHeadGear() {
        return headGear;
    }

    public Item getArmor() {
        return armor;
    }

    public Item getGauntlets() {
        return gauntlets;
    }

    public Item getBoots() {
        return boots;
    }

    public Item getCape() {
        return cape;
    }

    public Item getLeftHandedWeapon() {
        return leftHandedWeapon;
    }

    public Item getRightHandedWeapon() {
        return rightHandedWeapon;
    }


    public void setHeadGear(Item headGear) {
        this.headGear = headGear;
    }

    public void setArmor(Item armor) {
        this.armor = armor;
    }

    public void setGauntlets(Item gauntlets) {
        this.gauntlets = gauntlets;
    }

    public void setBoots(Item boots) {
        this.boots = boots;
    }

    public void setCape(Item cape) {
        this.cape = cape;
    }

    public void setLeftHandedWeapon(Item leftHandedWeapon) {
        this.leftHandedWeapon = leftHandedWeapon;
    }

    public void setRightHandedWeapon(Item rightHandedWeapon) {
        this.rightHandedWeapon = rightHandedWeapon;
    }

}