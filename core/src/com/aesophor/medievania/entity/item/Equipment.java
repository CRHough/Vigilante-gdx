package com.aesophor.medievania.entity.item;

import com.aesophor.medievania.component.equipment.EquipmentDataComponent;
import com.badlogic.gdx.physics.box2d.World;

public class Equipment extends Item {

    public Equipment(String equipmentName, World world, Float x, Float y) {
        super(equipmentName, world, x, y);
        add(new EquipmentDataComponent());
    }

}