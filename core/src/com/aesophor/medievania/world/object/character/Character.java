package com.aesophor.medievania.world.object.character;

import com.aesophor.medievania.constant.Constants;
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
    protected boolean isAttacking;
    protected boolean isCrouching;
    protected boolean setToKill;
    protected boolean killed;
    
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
        if (!killed) {
            // If the character's health has reached zero but hasn't die yet,
            // it means that the killedAnimation is not fully played.
            // So here we'll play it until it's finished.
            if (setToKill) {
                setRegion(getFrame(delta));
                
                // Set killed to true to prevent further rendering updates.
                if (killedAnimation.isAnimationFinished(stateTimer)) {
                    currentWorld.destroyBody(b2body);
                    killed = true;
                }
            } else {
                setRegion(getFrame(delta));
                
                // Set isAttacking back to false, implying attack has complete.
                if (isAttacking() && stateTimer >= attackTime) {
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
            shape.setPosition(new Vector2((getX() - attackRange) / Constants.PPM, getY() / Constants.PPM));
        } else if (facingRight && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            CircleShape shape = (CircleShape) meleeAttackFixture.getShape();
            shape.setPosition(new Vector2((getX() + attackRange) / Constants.PPM, getY() / Constants.PPM));
        }
        
        stateTimer = (currentState != previousState) ? 0 : stateTimer + delta;
        return textureRegion;
    }
    
    public Character.State getState() {
        if (setToKill) {
            return Character.State.KILLED;
        } else if (isAttacking) {
            return Character.State.ATTACKING;
        } else if (b2body.getLinearVelocity().y > 0) {
            return Character.State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return Character.State.FALLING;
        } else if (isCrouching()) {
            return Character.State.CROUCHING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return Character.State.RUNNING;
        } else {
            return Character.State.IDLE;
        }
    }
    
    
    // Movement controls
    public void moveRight() {
        facingRight = true;
        
        if (b2body.getLinearVelocity().x <= movementSpeed * 2) {
            b2body.applyLinearImpulse(new Vector2(movementSpeed, 0), b2body.getWorldCenter(), true);
        }
    }
    
    public void moveLeft() {
        facingRight = false;
        
        if (b2body.getLinearVelocity().x >= -movementSpeed * 2) {
            b2body.applyLinearImpulse(new Vector2(-movementSpeed, 0), b2body.getWorldCenter(), true);
        }
    }
    
    public void jump() {
        if (isOnGround()) {
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
            
            if (isTargetInRange() && !inRangeTarget.isSetToKill()) {
                float force = (facingRight) ? attackForce : -attackForce;
                inRangeTarget.getB2Body().applyLinearImpulse(new Vector2(force, 0), inRangeTarget.getB2Body().getWorldCenter(), true);
                
                inflictDamage(inRangeTarget, attackDamage);
                weaponHitSound.play();
            }
            
            weaponSwingSound.play();
            return;
        }
    }
    
    public void inflictDamage(Character c, int damage) {
        c.receiveDamage(damage);
        c.setLockedOnTarget(this);
    }
    
    public void receiveDamage(int damage) {
        health -= damage;
        
        if (health <= 0) {
            Filter filter = new Filter();
            filter.categoryBits = Constants.DESTROYED_BIT;
            bodyFixture.setFilterData(filter);
            
            setToKill = true;
            deathSound.play();
        } else {
            hurtSound.play();
        }
    }
    
    
    protected void moveTowardTarget(Character c) {
        if (this.b2body.getPosition().x > c.getB2Body().getPosition().x) {
            moveLeft();
        } else {
            moveRight();
        }
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
    
    public int getHealth() {
        return health;
    }
    
    public boolean isSetToKill() {
        return setToKill;
    }
    
    public boolean isKilled() {
        return killed;
    }
    
    public boolean isOnGround() {
        return b2body.getLinearVelocity().y == 0;
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