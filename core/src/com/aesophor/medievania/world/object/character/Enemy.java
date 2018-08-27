package com.aesophor.medievania.world.object.character;

import com.aesophor.medievania.constants.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Character {
    
    protected boolean aggressive;
    
    public Enemy(Texture texture, World world, float x, float y) {
        super(texture, world, x, y);
    }
    
    
    @Override
    public void update(float delta) {
        super.update(delta);
        
        if (lastStoppedPosition == null) {
            lastStoppedPosition = new Vector2();
        }
        
        if (aggressive && hasLockedOnTarget() && !isSetToKill()) {
            // Swing his weapon if lockedOnTarget is in melee attack range.
            if (isTargetInRange()) {
                swingWeapon();
                
                if (lockedOnTarget.isSetToKill()) {
                    lockedOnTarget = null;
                }
            } else {
                // Track down the lockOnTarget until it is within melee attack range.
                if (getDistanceBetween(b2body.getPosition().x, lockedOnTarget.b2body.getPosition().x) >= attackRange / Constants.PPM) {
                    moveTowardTarget(lockedOnTarget);
                    jumpIfStucked(delta);
                }
                
            }
        } else {
            moveRandomly(delta);
        }
    }
    
    
    
    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        aggressive = true;
    }
    
    protected boolean isAggressive() {
        return aggressive;
    }
    
}