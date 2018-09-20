package com.aesophor.medievania.component.equipment;

public enum EquipmentSlot {

    LEFT_HANDED_WEAPON(EquipmentType.WEAPON),     // 0
    RIGHT_HANDED_WEAPON(EquipmentType.WEAPON),    // 1
    HEADGEAR(EquipmentType.HEADGEAR),             // 2
    ARMOR(EquipmentType.ARMOR),                   // 3
    GAUNTLETS(EquipmentType.GAUNTLETS),           // 4
    BOOTS(EquipmentType.BOOTS),                   // 5
    CAPE(EquipmentType.CAPE);                     // 6


    private final EquipmentType type;

    private EquipmentSlot(EquipmentType type) {
        this.type = type;
    }


    public EquipmentType getType() {
        return type;
    }

}