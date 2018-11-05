package com.aesophor.vigilante.component.equipment;

import com.badlogic.ashley.core.Component;

public class EquipmentDataComponent implements Component {

    private String atlas;
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