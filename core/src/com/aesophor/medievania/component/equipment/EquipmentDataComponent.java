package com.aesophor.medievania.component.equipment;

import com.aesophor.medievania.component.graphics.FrameData;
import com.badlogic.ashley.core.Component;

import java.util.HashMap;

public class EquipmentDataComponent implements Component {

    private String atlas;
    private int textureOffsetX;
    private int textureOffsetY;
    private int frameWidth;
    private int frameHeight;
    private HashMap<String, FrameData> frameData;

    private Integer type;
    private Integer bonusPhysicalDamage;
    private Integer bonusMagicalDamage;
    private Integer bonusStr;
    private Integer bonusDex;
    private Integer bonusInt;
    private Integer bonusLuk;


    public String getAtlas() {
        return atlas;
    }

    public int getTextureOffsetX() {
        return textureOffsetX;
    }

    public int getTextureOffsetY() {
        return textureOffsetY;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public HashMap<String, FrameData> getFrameData() {
        return frameData;
    }


    public EquipmentType getType() {
        return EquipmentType.values()[type];
    }

    public Integer getBonusPhysicalDamage() {
        return bonusPhysicalDamage;
    }

    public Integer getBonusMagicalDamage() {
        return bonusMagicalDamage;
    }

    public Integer getBonusStr() {
        return bonusStr;
    }

    public Integer getBonusDex() {
        return bonusDex;
    }

    public Integer getBonusInt() {
        return bonusInt;
    }

    public Integer getBonusLuk() {
        return bonusLuk;
    }

}