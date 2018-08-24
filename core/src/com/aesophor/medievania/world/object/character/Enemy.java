package com.aesophor.medievania.world.object.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Character {
    
    public Enemy(Texture texture, World world, float x, float y) {
        super(texture, world, x, y);
    }
    
}