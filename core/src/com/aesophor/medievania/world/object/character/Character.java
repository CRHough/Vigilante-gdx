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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Character extends Sprite {

    public enum State { IDLE, RUNNING, JUMPING, FALLING, ATTACKING, KILLED };
    
    
    protected Character.State currentState;
    protected Character.State previousState;
    
    protected World currentWorld; // The world in which the character is spawned.
    protected Body b2body;
    protected Fixture meleeAttackFixture;
    
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
    
    protected float stateTimer;
    protected boolean facingRight;
    protected boolean isAttacking;
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
    
    protected Character targetEnemy;
    
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
                //stateTimer += delta;
                //setRegion(killedAnimation.getKeyFrame(stateTimer, false));
                setRegion(getFrame(delta));
                
                if (killedAnimation.isAnimationFinished(stateTimer)) {
                    killed = true;
                    deathSound.play();
                    currentWorld.destroyBody(b2body);
                }
            } else {
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 10 / Constants.PPM - getHeight() / 2);
                setRegion(getFrame(delta));
                
                // Set isAttacking back to false, implying attack has complete.
                if (isAttacking() && stateTimer >= attackTime) {
                    isAttacking = false;
                    stateTimer = 0;
                }
            }
        }
    }
    
    public TextureRegion getFrame(float delta) {
        previousState = currentState;
        currentState = getState();
        
        TextureRegion textureRegion;
        switch (currentState) {
            case JUMPING:
                textureRegion = (TextureRegion) jumpAnimation.getKeyFrame(stateTimer, false);
                break;
            case RUNNING:
                textureRegion = (TextureRegion) runAnimation.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                textureRegion = (TextureRegion) attackAnimation.getKeyFrame(stateTimer, false);
                break;
            case KILLED:
                textureRegion = (TextureRegion) killedAnimation.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
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
        if (isAttacking) {
            return Character.State.ATTACKING;
        } else if (setToKill) {
            return Character.State.KILLED;
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
    
    
    // Movement controls
    public void walkRight() {
        facingRight = true;
        
        if (getB2Body().getLinearVelocity().x <= movementSpeed * 2) {
            getB2Body().applyLinearImpulse(new Vector2(movementSpeed, 0), getB2Body().getWorldCenter(), true);
        }
    }
    
    public void walkLeft() {
        facingRight = false;
        
        if (getB2Body().getLinearVelocity().x >= -movementSpeed * 2) {
            getB2Body().applyLinearImpulse(new Vector2(-movementSpeed, 0), getB2Body().getWorldCenter(), true);
        }
    }
    
    public void jump() {
        if (isOnGround()) {
            getB2Body().applyLinearImpulse(new Vector2(0, jumpHeight), getB2Body().getWorldCenter(), true);
            jumpSound.play();
        }
    }
    
    // Use stateTimer.
    public void swingWeapon() {
        if (!isAttacking()) {
            setIsAttacking(true);
            
            if (hasTargetEnemy() && !targetEnemy.isSetToKill()) {
                inflictDamage(getTargetEnemy(), attackDamage);
                weaponHitSound.play();
                
                float force = (facingRight) ? attackForce : -attackForce;
                getTargetEnemy().getB2Body().applyLinearImpulse(new Vector2(force, 0), getTargetEnemy().getB2Body().getWorldCenter(), true);
            }
            
            weaponSwingSound.play();
            return;
        }
    }
    
    public void inflictDamage(Character c, int damage) {
        c.receiveDamage(damage);
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


    public boolean hasTargetEnemy() {
        return targetEnemy != null;
    }
    
    public Character getTargetEnemy() {
        return targetEnemy;
    }
    
    public void setTargetEnemy(Character enemy) {
        targetEnemy = enemy;
    }
    
    
    public boolean facingRight() {
        return facingRight;
    }
    
}