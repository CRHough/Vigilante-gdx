package com.aesophor.medievania.world.object.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Character extends Sprite {

    public enum State { IDLE, RUNNING, JUMPING, FALLING, ATTACKING };
    
    
    protected Character.State currentState;
    protected Character.State previousState;
    
    protected World currentWorld; // The world in which the character is spawned.
    protected Body b2body;
    
    protected TextureRegion idleAnimation; // Change to Animation later.
    protected Animation<TextureRegion> runAnimation;
    protected Animation<TextureRegion> jumpAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> killedAnimation;
    
    protected Music footstepSound; // dispose() these sound later.
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
    
    protected int health;
    protected int attackRange;
    
    public Character(Texture texture, World currentWorld, float x, float y) {
        super(texture);
        this.currentWorld = currentWorld;
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
        
        hurtSound.play();
    }
    
    
    
    // Review the code below.
    public Body getB2Body() {
        return b2body;
    }
    
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
    
    public void setIsAttacking(boolean isAttacking) {
        weaponSwingSound.play();
        this.isAttacking = isAttacking;
    }


    public boolean hasTargetEnemy() {
        return !(targetEnemy == null);
    }
    
    public Character getTargetEnemy() {
        return targetEnemy;
    }
    
    public void setTargetEnemy(Character enemy) {
        targetEnemy = enemy;
    }
    
    public void attack(Character c) {
        Gdx.app.log("Player", String.format("deals %d damage to %s", 25, "knight"));
        c.receiveDamage(25);
        
        weaponHitSound.play();
        
        float force = (facingRight) ? .6f : -.6f;
        //c.b2body.applyLinearImpulse(new Vector2(force, 0), enemy.b2body.getWorldCenter(), true);
    }
    
    public boolean facingRight() {
        return facingRight;
    }
    
}