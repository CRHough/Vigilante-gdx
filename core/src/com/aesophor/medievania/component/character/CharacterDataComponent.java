package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;

public class CharacterDataComponent implements Component {

    private String atlas;
    private int textureOffsetX;
    private int textureOffsetY;

    private String hurtSound;
    private String killedSound;
    private String weaponSwingSound;
    private String weaponHitSound;
    private String jumpSound;
    private String itemPickedupSound;

    private StatsComponent stats;
    private HashMap<String, Float> items;

    public String getAtlas() {
        return atlas;
    }

    public int getTextureOffsetX() {
        return textureOffsetX;
    }

    public int getTextureOffsetY() {
        return textureOffsetY;
    }


    public String getHurtSound() {
        return hurtSound;
    }

    public String getKilledSound() {
        return killedSound;
    }

    public String getWeaponHitSound() {
        return weaponHitSound;
    }

    public String getWeaponSwingSound() {
        return weaponSwingSound;
    }

    public String getJumpSound() {
        return jumpSound;
    }

    public String getItemPickedupSound() {
        return itemPickedupSound;
    }


    public StatsComponent getStats() {
        return stats;
    }

    public HashMap<String, Float> getItems() {
        return items;
    }

}