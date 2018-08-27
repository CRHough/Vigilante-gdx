package com.aesophor.medievania.world.object.character;

import java.util.concurrent.ThreadLocalRandom;
import com.aesophor.medievania.constants.CategoryBits;
import com.aesophor.medievania.constants.Constants;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Character extends Sprite {

    public enum State { IDLE, RUNNING, JUMPING, FALLING, CROUCHING, ATTACKING, KILLED };
    
    
    protected Character.State currentState;
    protected Character.State previousState;
    
    protected World currentWorld; // The world in which the character is spawned.
    protected Body b2body;
    protected Fixture bodyFixture;
    protected Fixture meleeAttackFixture;
    
    protected TextureRegion idleAnimation; // Change to Animation later.
    protected Animation<TextureRegion> runAnimation;
    protected Animation<TextureRegion> jumpAnimation;
    protected Animation<TextureRegion> fallAnimation;
    protected Animation<TextureRegion> crouchAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> killedAnimation;
    
    protected Music footstepSound; // dispose() these sound later.
    protected Sound hurtSound;
    protected Sound deathSound;
    protected Sound weaponSwingSound;
    protected Sound weaponHitSound;
    protected Sound jumpSound;
    
    protected float stateTimer;
    protected boolean facingRight;
    protected boolean isJumping;
    protected boolean isAttacking;
    protected boolean isCrouching;
    protected boolean isUntouchable;
    protected boolean isKilled;
    protected boolean setToKill;
    
    protected String name;
    protected int level;
    protected int health;
    protected int stamina;
    protected int magicka;
    
    protected float movementSpeed;
    protected float jumpHeight;
    
    protected float attackForce;
    protected float attackTime;
    protected int attackRange;
    protected int attackDamage;
    
    protected Character lockedOnTarget;
    protected Character inRangeTarget;
    
    protected int moveDirection;
    protected float moveDuration;
    protected float sleepDuration;
    protected float moveDurationTimer; // oh god, refactor this part later.
    protected float sleepDurationTimer;
    
    protected Vector2 lastStoppedPosition;
    protected float lastTraveledDistance;
    protected float calulateDistanceTimer;
    
    public Character(Texture texture, World currentWorld, float x, float y) {
        super(texture);
        
        this.currentWorld = currentWorld;
        setPosition(x, y);
        
        currentState = Character.State.IDLE;
        previousState = Character.State.IDLE;
        
        stateTimer = 0;
        facingRight = true;
    }
    
    
    protected abstract void defineBody();
    
    protected void defineMeleeFixture() {
        
    }
    
    public void update(float delta) {
        if (!isKilled) {
            // If the character's health has reached zero but hasn't die yet,
            // it means that the killedAnimation is not fully played.
            // So here we'll play it until it's finished.
            if (setToKill) {
                setRegion(getFrame(delta));
                
                // Set killed to true to prevent further rendering updates.
                if (killedAnimation.isAnimationFinished(stateTimer)) {
                    currentWorld.destroyBody(b2body);
                    isKilled = true;
                }
            } else {
                setRegion(getFrame(delta));
                
                // Set isAttacking back to false, implying attack has complete.
                if (isAttacking && stateTimer >= attackTime) {
                    isAttacking = false;
                    stateTimer = 0;
                }
            }
            
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 10 / Constants.PPM - getHeight() / 2);
        }
    }
    
    public TextureRegion getFrame(float delta) {
        previousState = currentState;
        currentState = getState();
        
        TextureRegion textureRegion;
        switch (currentState) {
            case RUNNING:
                textureRegion = (TextureRegion) runAnimation.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                textureRegion = (TextureRegion) jumpAnimation.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
                textureRegion = (TextureRegion) fallAnimation.getKeyFrame(stateTimer, false);
                break;
            case CROUCHING:
                textureRegion = (TextureRegion) crouchAnimation.getKeyFrame(stateTimer, false);
                break;
            case ATTACKING:
                textureRegion = (TextureRegion) attackAnimation.getKeyFrame(stateTimer, false);
                break;
            case KILLED:
                textureRegion = (TextureRegion) killedAnimation.getKeyFrame(stateTimer, false);
                break;
            case IDLE:
            default:
                textureRegion = idleAnimation;
                break;
        }
        
        if (!facingRight && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false); // flip x, flip y.
            CircleShape shape = (CircleShape) meleeAttackFixture.getShape();
            shape.setPosition(new Vector2(-attackRange / Constants.PPM, 0));
        } else if (facingRight && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            CircleShape shape = (CircleShape) meleeAttackFixture.getShape();
            shape.setPosition(new Vector2(attackRange / Constants.PPM, 0));
        }
        
        stateTimer = (currentState != previousState) ? 0 : stateTimer + delta;
        return textureRegion;
    }
    
    public Character.State getState() {
        if (setToKill) {
            return Character.State.KILLED;
        } else if (isAttacking) {
            return Character.State.ATTACKING;
        } else if (b2body.getLinearVelocity().y >= -.5f && previousState == Character.State.JUMPING) {
            return Character.State.JUMPING;
        } else if (b2body.getLinearVelocity().y < -.5f) {
            return Character.State.FALLING;
        } else if (isCrouching()) {
            return Character.State.CROUCHING;
        } else if (b2body.getLinearVelocity().x > .01f || b2body.getLinearVelocity().x < -.01f) {
            return Character.State.RUNNING;
        } else {
            return Character.State.IDLE;
        }
    }
    
    
    // Movement controls
    public void moveLeft() {
        facingRight = false;
        
        if (b2body.getLinearVelocity().x >= -movementSpeed * 2) {
            b2body.applyLinearImpulse(new Vector2(-movementSpeed, 0), b2body.getWorldCenter(), true);
        }
    }
    
    public void moveRight() {
        facingRight = true;
        
        if (b2body.getLinearVelocity().x <= movementSpeed * 2) {
            b2body.applyLinearImpulse(new Vector2(movementSpeed, 0), b2body.getWorldCenter(), true);
        }
    }
    
    public void jump() {
        if (!isJumping) {
            isJumping = true;
            
            getB2Body().applyLinearImpulse(new Vector2(0, jumpHeight), b2body.getWorldCenter(), true);
            jumpSound.play();
        }
    }
    
    public void crouch() {
        if (!isCrouching) {
            isCrouching = true;
        }
    }
    
    public void getUp() {
        if (isCrouching) {
            isCrouching = false;
        }
    }
    
    
    public void swingWeapon() {
        if (!isAttacking()) {
            setIsAttacking(true);
            
            if (isTargetInRange() &&!inRangeTarget.isUntouchable() && !inRangeTarget.isSetToKill()) {
                setLockedOnTarget(inRangeTarget);
                inflictDamage(inRangeTarget, attackDamage);
                
                if (isTargetInRange()) {
                    inRangeTarget.setLockedOnTarget(this);
                    weaponHitSound.play();
                }
            }
            
            weaponSwingSound.play();
            return;
        }
    }
    
    public void inflictDamage(Character c, int damage) {
        c.receiveDamage(damage);
        c.pushedBackward((facingRight) ? attackForce : -attackForce);
    }
    
    public void receiveDamage(int damage) {
        if (isUntouchable) return;
        
        health -= damage;
        
        if (health <= 0) {
            setCategoryBits(bodyFixture, CategoryBits.UNTOUCHABLE);
            setToKill = true;
            deathSound.play();
        } else {
            hurtSound.play();
        }
    }
    
    public void pushedBackward(float force) {
        b2body.applyLinearImpulse(new Vector2(force, 1f), b2body.getWorldCenter(), true);
    }
    
    public static void setCategoryBits(Fixture f, short bits) {
        Filter filter = new Filter();
        filter.categoryBits = bits;
        f.setFilterData(filter);
    }
    
    
    protected void moveTowardTarget(Character c) {
        // add response to cliff.
        if (this.b2body.getPosition().x > c.getB2Body().getPosition().x) {
            moveLeft();
        } else {
            moveRight();
        }
    }
    
    protected void moveRandomly(float delta) {
        if (sleepDurationTimer >= sleepDuration) {
            moveDirection = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            moveDuration = ThreadLocalRandom.current().nextInt(0, 5 + 1);
            sleepDuration = ThreadLocalRandom.current().nextInt(0, 5 + 1);
            
            moveDurationTimer = 0;
            sleepDurationTimer = 0;
        }
        
        switch (moveDirection) {
            case 0:
                if (moveDurationTimer < moveDuration) {
                    moveLeft();
                    jumpIfStucked(delta);
                    moveDurationTimer += delta;
                } else {
                    if (sleepDurationTimer < sleepDuration) {
                        sleepDurationTimer += delta;
                    }
                }
                break;
                
            case 1:
                if (moveDurationTimer < moveDuration) {
                    moveRight();
                    jumpIfStucked(delta);
                    moveDurationTimer += delta;
                } else {
                    if (sleepDurationTimer < sleepDuration) {
                        sleepDurationTimer += delta;
                    }
                }
                break;
                
            default:
                break;
        }
    }
    
    public void reverseMovement() { // rename this perhaps?
        moveDirection = (moveDirection == 0) ? 1 : 0;
    }
    
    
    protected void jumpIfStucked(float delta) {
        if (calulateDistanceTimer > 5f / Constants.PPM) {
            lastTraveledDistance = getDistanceBetween(b2body.getPosition().x, lastStoppedPosition.x);
            lastStoppedPosition.set(b2body.getPosition());
            
            if (lastTraveledDistance == 0) {
                //isJumping = false; // will this help? is it necessary?
                jump();
            }
            
            calulateDistanceTimer = 0;
        } else {
            calulateDistanceTimer += delta;
        }
    }
    
    public static float getDistanceBetween(float x1, float x2) {
        float distance = x1 - x2;
        return (distance > 0) ? distance : -distance; 
    }
    
    
    
    
    
    // Review the code below.
    public Body getB2Body() {
        return b2body;
    }
    
    public Fixture getBodyFixture() {
        return bodyFixture;
    }
    
    
    public boolean isAttacking() {
        return isAttacking;
    }
    
    public boolean isCrouching() {
        return isCrouching;
    }
    
    public boolean isUntouchable() {
        return isUntouchable;
    }
    
    public int getHealth() {
        return health;
    }
    
    public boolean isSetToKill() {
        return setToKill;
    }
    
    public boolean isKilled() {
        return isKilled;
    }
    
    public boolean isJumping() {
        return isJumping;
    }
    
    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }
    
    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }


    public boolean hasLockedOnTarget() {
        return lockedOnTarget != null;
    }
    
    public boolean isTargetInRange() {
        return inRangeTarget != null;
    }
    
    public Character getLockedOnTarget() {
        return lockedOnTarget;
    }
    
    public Character getInRangeTarget() {
        return inRangeTarget;
    }
    
    public void setLockedOnTarget(Character enemy) {
        lockedOnTarget = enemy;
    }
    
    public void setInRangeTarget(Character enemy) {
        inRangeTarget = enemy;
    }
    
    
    public boolean facingRight() {
        return facingRight;
    }
    
}