package com.aesophor.medievania.equipment;

public class Axe implements Wieldable {

    private int baseDamage = 25;

    @Override
    public int getBaseDamage() {
        return baseDamage;
    }

}
