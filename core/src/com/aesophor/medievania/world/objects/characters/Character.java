package com.aesophor.medievania.world.objects.characters;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Character extends Sprite {

    public enum State { IDLE, RUNNING, JUMPING, FALLING, ATTACKING };
    
    
    protected int health;
    
    protected Character.State currentState;
    protected Character.State previousState;
    
    protected World currentWorld; // The world in which the character is spawned.
    public Body b2body; // Character's body. temporarily set to public.
    
    protected TextureRegion idleAnimation; // Change to Animation later.
    protected Animation<TextureRegion> runAnimation;
    protected Animation<TextureRegion> jumpAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> killedAnimation;
    
    protected Music footstepSound;
    protected Sound hurtSound;
    protected Sound deathSound;
    protected Sound weaponSwingSound;
    protected Sound weaponHitSound;
    protected Sound jumpSound;
    
    protected boolean facingRight;
    protected boolean isJumping;
    protected boolean isAttacking;
    protected boolean setToKill;
    protected boolean killed;
    
    protected float stateTimer;
    protected Character targetEnemy;
    
    public Character(TextureRegion textureRegion, float x, float y) {
        super(textureRegion);
        setPosition(x, y);
        
        currentState = Character.State.IDLE;
        previousState = Character.State.IDLE;
        
        stateTimer = 0;
        facingRight = true;
    }
    
    protected abstract void defineCharacter();
    public abstract void update(float dt);
    
    public TextureRegion getFrame(float dt) {
        currentState = getState();
        
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) jumpAnimation.getKeyFrame(stateTimer, false);
                break;
            case RUNNING:
                region = (TextureRegion) runAnimation.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = (TextureRegion) attackAnimation.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
            case IDLE:
            default:
                region = idleAnimation;
                break;
        }
        
        if ((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false); // flip x, flip y.
            facingRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }
        
        if (isAttacking() && attackAnimation.isAnimationFinished(stateTimer) && stateTimer >= 1) {
            isAttacking = false;
        }
        
        stateTimer = (currentState == previousState) ? stateTimer + dt : 0;
        previousState = currentState;
        
        return region;
    }
    
    public Character.State getState() {
        if (isAttacking()) {
            return Character.State.ATTACKING;
        } else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == Character.State.JUMPING)) {
            return Character.State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return Character.State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return Character.State.RUNNING;
        } else {
            return Character.State.IDLE;
        }
    }
    
    public void receiveDamage(int damage) {
        health -= damage;
        
        if (health <= 0) {
            setToKill = true;
        }
    }
    
    // Review the code below.
    public boolean isAttacking() {
        return isAttacking;
    }
    
    public int getHealth() {
        return health;
    }
    
    public boolean killed() {
        return killed;
    }
    
    public boolean isJumping() {
        return b2body.getLinearVelocity().y != 0;
    }
    
    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
        jumpSound.play();
    }
    
}