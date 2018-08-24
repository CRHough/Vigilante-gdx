package com.aesophor.medievania.world.object.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Character {
    
    protected Player player;
    
    public Enemy(Texture texture, World world, float x, float y) {
        super(texture, world, x, y);
    }
    
    protected Vector2 getPlayerPosition() {
        return player.getB2Body().getPosition();
    }
    
}