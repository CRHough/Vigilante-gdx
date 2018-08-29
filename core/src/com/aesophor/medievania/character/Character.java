package com.aesophor.medievania.character;

import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Rumble;
import com.aesophor.medievania.util.CategoryBits;
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
import com.badlogic.gdx.utils.Disposable;

public abstract class Character extends Sprite implements Disposable {

    public enum State { IDLE, RUNNING, JUMPING, FALLING, CROUCHING, ATTACKING, KILLED };

    
    protected Character.State currentState;
    protected Character.State previousState;

    protected World currentWorld;
    protected Body b2body;
    protected Fixture bodyFixture;
    protected Fixture meleeWeaponFixture;
    
    protected TextureRegion idleAnimation; // Change to Animation later.
    protected Animation<TextureRegion> runAnimation;
    protected Animation<TextureRegion> jumpAnimation;
    protected Animation<TextureRegion> fallAnimation;
    protected Animation<TextureRegion> crouchAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> killedAnimation;
    
    protected Music footstepSound;
    protected Sound hurtSound;
    protected Sound deathSound;
    protected Sound weaponSwingSound;
    protected Sound weaponHitSound;
    protected Sound jumpSound;
    
    protected float stateTimer;
    protected boolean isAlerted;
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

    protected Behavior behavior;
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

        behavior = new Behavior(this);
    }
    
    
    protected abstract void defineBody();
    
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

            float textureX = b2body.getPosition().x - getWidth() / 2;
            float textureY = b2body.getPosition().y - getHeight() / 2 + (10 / Constants.PPM);
            setPosition(textureX, textureY);
        }
    }
    
    private TextureRegion getFrame(float delta) {
        previousState = currentState;
        currentState = getState();
        
        TextureRegion textureRegion;
        switch (currentState) {
            case RUNNING:
                textureRegion = runAnimation.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                textureRegion = jumpAnimation.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
                textureRegion = fallAnimation.getKeyFrame(stateTimer, false);
                break;
            case CROUCHING:
                textureRegion = crouchAnimation.getKeyFrame(stateTimer, false);
                break;
            case ATTACKING:
                textureRegion = attackAnimation.getKeyFrame(stateTimer, false);
                break;
            case KILLED:
                textureRegion = killedAnimation.getKeyFrame(stateTimer, false);
                break;
            case IDLE:
            default:
                textureRegion = idleAnimation;
                break;
        }
        
        if (!facingRight && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            CircleShape shape = (CircleShape) meleeWeaponFixture.getShape();
            shape.setPosition(new Vector2(-attackRange / Constants.PPM, 0));
        } else if (facingRight && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            CircleShape shape = (CircleShape) meleeWeaponFixture.getShape();
            shape.setPosition(new Vector2(attackRange / Constants.PPM, 0));
        }
        
        stateTimer = (currentState != previousState) ? 0 : stateTimer + delta;
        return textureRegion;
    }
    
    private Character.State getState() {
        if (setToKill) {
            return Character.State.KILLED;
        } else if (isAttacking) {
            return Character.State.ATTACKING;
        } else if (isJumping) {
            return Character.State.JUMPING;
        } else if (b2body.getLinearVelocity().y < -.5f) {
            return Character.State.FALLING;
        } else if (isCrouching) {
            return Character.State.CROUCHING;
        } else if (b2body.getLinearVelocity().x > .01f || b2body.getLinearVelocity().x < -.01f) {
            return Character.State.RUNNING;
        } else {
            return Character.State.IDLE;
        }
    }
    

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

            if (hasInRangeTarget() && !inRangeTarget.isUntouchable() && !inRangeTarget.isSetToKill()) {
                setLockedOnTarget(inRangeTarget);
                inflictDamage(inRangeTarget, attackDamage);
                
                inRangeTarget.setLockedOnTarget(this);
                weaponHitSound.play();
            }
            
            weaponSwingSound.play();
            return;
        }
    }
    
    public void inflictDamage(Character c, int damage) {
        if (this.getClass().equals(Player.class)) {
            Rumble.rumble(8 / Constants.PPM, .1f);
        }
        
        c.receiveDamage(damage);
        c.pushedBackward((facingRight) ? attackForce : -attackForce);
    }
    
    public void receiveDamage(int damage) {
        if (this.getClass().equals(Player.class)) {
            Rumble.rumble(8 / Constants.PPM, .1f);
        }

        if (!isUntouchable) {
            health -= damage;

            if (health <= 0) {
                setCategoryBits(bodyFixture, CategoryBits.UNTOUCHABLE);
                setToKill = true;
                deathSound.play();
            } else {
                hurtSound.play();
            }
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
    
    public boolean hasInRangeTarget() {
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

    public Behavior getBehavior() {
        return behavior;
    }
    
    @Override
    public void dispose() {
        footstepSound.dispose();
        hurtSound.dispose();
        deathSound.dispose();
        weaponSwingSound.dispose();
        weaponHitSound.dispose();
        jumpSound.dispose();
    }
    
}