package com.aesophor.medievania.entity.item.equipment;

import com.aesophor.medievania.component.ItemType;
import com.aesophor.medievania.entity.item.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Axe extends Item {

    private int baseDamage = 25;

    public Axe(Texture texture, World world, float x, float y) {
        super(ItemType.EQUIPMENT, texture, world, x, y);
    }

    @Override
    public void dispose() {

    }

}
