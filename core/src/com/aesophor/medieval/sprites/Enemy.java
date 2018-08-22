package com.aesophor.medieval.sprites;

import com.aesophor.medieval.screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    
    protected World world; // the world to spawn in.
    protected GameScreen screen;
    
    public Body b2body;
    
    public Enemy(World world, GameScreen screen, float x, float y) {
        
        this.world = world;
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }

    
    protected abstract void defineEnemy();
    
    public abstract void update(float dt);
    
}
