package com.aesophor.medievania.world.objects.characters;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.screens.GameScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Character {
    
    protected World world; // the world to spawn in.
    protected GameScreen screen;
    
    public Body b2body;
    
    protected boolean aggresive;
    
    protected Sound hurtSound;
    protected Sound deathSound;
    
    public Enemy(GameScreen screen, float x, float y) {
        super(screen.getAtlas().findRegion("bandit"), x, y); // clean up this part later.
        this.screen = screen;
        
        world = screen.getWorld();
        
        
        health = 100;
        
        defineCharacter();
        
        hurtSound = Medievania.manager.get("Sound/FX/Player/hurt.wav");
        deathSound = Medievania.manager.get("Sound/FX/Player/death.mp3");
    }

    @Override
    protected abstract void defineCharacter();
    
    @Override
    public abstract void update(float dt);

    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
    }
    
}
