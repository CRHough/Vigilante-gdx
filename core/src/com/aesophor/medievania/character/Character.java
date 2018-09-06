package com.aesophor.medievania.character;

import com.aesophor.medievania.component.*;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;

public abstract class Character extends Entity implements Disposable {

    protected CharacterStatsComponent cc;
    protected B2BodyComponent bc;
    protected SpriteComponent spc;

    protected AnimationComponent ac;
    protected StateComponent stc;

    protected World currentWorld;
    
    protected Music footstepSound;
    protected Sound hurtSound;
    protected Sound deathSound;
    protected Sound weaponSwingSound;
    protected Sound weaponHitSound;
    protected Sound jumpSound;

    protected BehavioralModel behavioralModel;
    protected Character lockedOnTarget;
    protected Character inRangeTarget;

    protected Queue<Actor> damageIndicators; // not removing expired ones yet.

    public Character(Texture texture, World currentWorld, float x, float y) {
        this.currentWorld = currentWorld;

        cc = new CharacterStatsComponent();
        ac = new AnimationComponent();
        bc = new B2BodyComponent(currentWorld);
        spc = new SpriteComponent(texture, x, y);
        stc = new StateComponent(State.IDLE);

        behavioralModel = new BehavioralModel(this);

        damageIndicators = new Queue<>();
    }


    protected void defineBody(BodyDef.BodyType type, short bodyCategoryBits, short bodyMaskBits, short feetMaskBits, short meleeWeaponMaskBits) {
        bc.body = bc.bodyBuilder.type(type)
                .position(spc.sprite.getX(), spc.sprite.getY(), Constants.PPM)
                .buildBody();

        createBodyFixture(bodyCategoryBits, bodyMaskBits);
        createFeetFixture(feetMaskBits);
        createMeleeWeaponFixture(meleeWeaponMaskBits);
    }

    /**
     * Builds body fixture based on this character's body width and height.
     * @param categoryBits category bits of body fixture.
     * @param maskBits defines which objects the body fixture can collide with.
     */
    protected void createBodyFixture(short categoryBits, short maskBits) {
        bc.bodyFixture = bc.bodyBuilder.newRectangleFixture(bc.body.getPosition(), cc.bodyWidth / 2, cc.bodyHeight / 2, Constants.PPM)
                .categoryBits(categoryBits)
                .maskBits(maskBits)
                .setUserData(this)
                .buildFixture();
    }

    /**
     * Builds feet fixture which is a bit lower than the body fixture.
     * @param maskBits defines which objects the feet fixture can collide with.
     */
    protected void createFeetFixture(short maskBits) {
        Vector2[] feetPolyVertices = new Vector2[4];
        feetPolyVertices[0] = new Vector2(-cc.bodyWidth / 2 + 1, -cc.bodyHeight / 2);
        feetPolyVertices[1] = new Vector2(cc.bodyWidth / 2 - 1, -cc.bodyHeight / 2);
        feetPolyVertices[2] = new Vector2(-cc.bodyWidth / 2 + 1, -cc.bodyHeight / 2 - 2);
        feetPolyVertices[3] = new Vector2(cc.bodyWidth / 2 - 1, -cc.bodyHeight / 2 - 2);

        bc.feetFixture = bc.bodyBuilder.newPolygonFixture(feetPolyVertices, Constants.PPM)
                .categoryBits(CategoryBits.FEET)
                .maskBits(maskBits)
                .isSensor(true)
                .setUserData(this)
                .buildFixture();
    }

    protected void createMeleeWeaponFixture(short maskBits) {
        Vector2 meleeAttackFixturePosition = new Vector2(cc.attackRange, 0);

        bc.meleeWeaponFixture = bc.bodyBuilder.newCircleFixture(meleeAttackFixturePosition, cc.attackRange, Constants.PPM)
                .categoryBits(CategoryBits.MELEE_WEAPON)
                .maskBits(maskBits)
                .isSensor(true)
                .setUserData(this)
                .buildFixture();
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

    public void jumpDown() {
        if (isOnPlatform) {
            isOnPlatform = false;
            b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y - 8f / Constants.PPM, 0);
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

            if (hasInRangeTarget() && !inRangeTarget.isInvincible() && !inRangeTarget.isSetToKill()) {
                setLockedOnTarget(inRangeTarget);
                inRangeTarget.setLockedOnTarget(this);

                inflictDamage(inRangeTarget, attackDamage);
                weaponHitSound.play();
            }
            
            weaponSwingSound.play();
            return;
        }
    }
    
    public void inflictDamage(Character c, int damage) {
        c.receiveDamage(damage);
        c.knockedBack((facingRight) ? attackForce : -attackForce);
    }
    
    public void receiveDamage(int damage) {
        if (!isInvincible) {
            health -= damage;

            if (health <= 0) {
                setCategoryBits(bodyFixture, CategoryBits.DESTROYED);
                setToKill = true;
                deathSound.play();
            } else {
                hurtSound.play();
            }
        }
    }
    
    public void knockedBack(float force) {
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

    public String getName() {
        return name;
    }
    
    public boolean isAttacking() {
        return isAttacking;
    }
    
    public boolean isCrouching() {
        return isCrouching;
    }
    
    public boolean isInvincible() {
        return isInvincible;
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

    public boolean isOnPlatform() {
        return isOnPlatform;
    }

    public void setIsOnPlatform(boolean isOnPlatform) {
        this.isOnPlatform = isOnPlatform;
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

    public BehavioralModel getBehavioralModel() {
        return behavioralModel;
    }

    public Queue<Actor> getDamageIndicators() {
        return damageIndicators;
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