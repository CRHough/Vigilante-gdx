package com.aesophor.medieval.sprites;

import com.aesophor.medieval.Medieval;
import com.aesophor.medieval.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    
    protected World world; // the world to spawn in.
    protected GameScreen screen;
    
    public Body b2body;
    
    protected int health;
    
    protected boolean setToKill;
    protected boolean killed;
    
    protected Sound hurtSound;
    protected Sound deathSound;
    
    public Enemy(World world, GameScreen screen, float x, float y) {
        
        this.world = world;
        this.screen = screen;
        setPosition(x, y);
        
        health = 100;
        
        defineEnemy();
        
        hurtSound = Medieval.manager.get("Sound/FX/Player/hurt.wav");
        deathSound = Medieval.manager.get("Sound/FX/Player/death.mp3");
    }

    
    protected abstract void defineEnemy();
    
    public abstract void update(float dt);


    public void inflictDamage(int damage) {
        health -= damage;
        
        if (health <= 0) {
            setToKill = true;
        }
        
        hurtSound.play();
    };
    
}
