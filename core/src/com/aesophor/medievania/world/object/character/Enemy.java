package com.aesophor.medievania.world.object.character;

import com.aesophor.medievania.constant.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Character {
    
    protected boolean aggressive;
    
    protected Vector2 lastStoppedPosition;
    protected float lastTraveledDistance;
    protected float calulateDistanceTimer;
    
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
                    
                    if (calulateDistanceTimer > 2f) {
                        lastTraveledDistance = getDistanceBetween(b2body.getPosition().x, lastStoppedPosition.x);
                        lastStoppedPosition.set(b2body.getPosition());
                        
                        if (lastTraveledDistance == 0) {
                            jump();
                        }
                        
                        calulateDistanceTimer = 0;
                    } else {
                        calulateDistanceTimer += delta;
                    }
                }
                
            }
        }
        
    }
    
    public static float getDistanceBetween(float x1, float x2) {
        float distance = x1 - x2;
        return (distance > 0) ? distance : -distance; 
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