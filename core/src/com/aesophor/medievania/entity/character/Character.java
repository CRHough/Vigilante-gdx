package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.*;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;

public abstract class Character extends Entity implements Disposable {

    protected SpriteComponent sprite;
    protected AnimationComponent animations;
    protected B2BodyComponent b2body;
    protected StateComponent state;
    protected SoundComponent sounds;
    protected CharacterStatsComponent stats;
    protected CombatTargetComponent targets;

    protected AIActions AIActions;
    protected Queue<Actor> damageIndicators; // not removing expired ones yet.

    public Character(Texture texture, World currentWorld, float x, float y) {
        stats = new CharacterStatsComponent();
        animations = new AnimationComponent();
        b2body = new B2BodyComponent(currentWorld);
        sprite = new SpriteComponent(texture, x, y);
        state = new StateComponent(State.IDLE);
        sounds = new SoundComponent();
        targets = new CombatTargetComponent();

        add(stats);
        add(animations);
        add(b2body);
        add(sprite);
        add(state);
        add(targets);

        AIActions = new AIActions(this);
        damageIndicators = new Queue<>();
    }


    protected void defineBody(BodyDef.BodyType type, short bodyCategoryBits, short bodyMaskBits, short feetMaskBits, short meleeWeaponMaskBits) {
        b2body.body = b2body.bodyBuilder.type(type)
                .position(sprite.sprite.getX(), sprite.sprite.getY(), Constants.PPM)
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
        b2body.bodyFixture = b2body.bodyBuilder.newRectangleFixture(b2body.body.getPosition(), stats.bodyWidth / 2, stats.bodyHeight / 2, Constants.PPM)
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
        feetPolyVertices[0] = new Vector2(-stats.bodyWidth / 2 + 1, -stats.bodyHeight / 2);
        feetPolyVertices[1] = new Vector2(stats.bodyWidth / 2 - 1, -stats.bodyHeight / 2);
        feetPolyVertices[2] = new Vector2(-stats.bodyWidth / 2 + 1, -stats.bodyHeight / 2 - 2);
        feetPolyVertices[3] = new Vector2(stats.bodyWidth / 2 - 1, -stats.bodyHeight / 2 - 2);

        b2body.feetFixture = b2body.bodyBuilder.newPolygonFixture(feetPolyVertices, Constants.PPM)
                .categoryBits(CategoryBits.FEET)
                .maskBits(maskBits)
                .isSensor(true)
                .setUserData(this)
                .buildFixture();
    }

    protected void createMeleeWeaponFixture(short maskBits) {
        Vector2 meleeAttackFixturePosition = new Vector2(stats.attackRange, 0);

        b2body.meleeWeaponFixture = b2body.bodyBuilder.newCircleFixture(meleeAttackFixturePosition, stats.attackRange, Constants.PPM)
                .categoryBits(CategoryBits.MELEE_WEAPON)
                .maskBits(maskBits)
                .isSensor(true)
                .setUserData(this)
                .buildFixture();
    }


    public void moveLeft() {
        state.facingRight = false;

        if (b2body.body.getLinearVelocity().x >= -stats.movementSpeed * 2) {
            b2body.body.applyLinearImpulse(new Vector2(-stats.movementSpeed, 0), b2body.body.getWorldCenter(), true);
        }
    }

    public void moveRight() {
        state.facingRight = true;

        if (b2body.body.getLinearVelocity().x <= stats.movementSpeed * 2) {
            b2body.body.applyLinearImpulse(new Vector2(stats.movementSpeed, 0), b2body.body.getWorldCenter(), true);
        }
    }

    public void jump() {
        if (!state.jumping) {
            state.jumping = true;

            getB2Body().applyLinearImpulse(new Vector2(0, stats.jumpHeight), b2body.body.getWorldCenter(), true);
            sounds.get(SoundType.JUMP).play();
        }
    }

    public void jumpDown() {
        if (state.onPlatform) {
            state.onPlatform = false;
            b2body.body.setTransform(b2body.body.getPosition().x, b2body.body.getPosition().y - 8f / Constants.PPM, 0);
        }
    }

    public void crouch() {
        if (!state.crouching) {
            state.crouching = true;
        }
    }

    public void getUp() {
        if (state.crouching) {
            state.crouching = false;
        }
    }


    public void swingWeapon() {
        if (!state.isAttacking()) {
            state.attacking = true;

            if (targets.hasInRangeTarget() && !targets.inRangeTarget.state.isInvincible() && !targets.inRangeTarget.state.isSetToKill()) {
                setLockedOnTarget(targets.inRangeTarget);
                targets.inRangeTarget.setLockedOnTarget(this);

                inflictDamage(targets.inRangeTarget, stats.attackDamage);
                sounds.get(SoundType.WEAPON_HIT).play();
            }

            sounds.get(SoundType.WEAPON_SWING).play();
            return;
        }
    }

    public void inflictDamage(Character c, int damage) {
        c.receiveDamage(damage);
        c.knockedBack((state.facingRight) ? stats.attackForce : -stats.attackForce);
    }

    public void receiveDamage(int damage) {
        if (!state.invincible) {
            stats.health -= damage;

            if (stats.health <= 0) {
                setCategoryBits(b2body.bodyFixture, CategoryBits.DESTROYED);
                state.setToKill = true;
                sounds.get(SoundType.DEATH).play();
            } else {
                sounds.get(SoundType.HURT).play();
            }
        }
    }

    public void knockedBack(float force) {
        b2body.body.applyLinearImpulse(new Vector2(force, 1f), b2body.body.getWorldCenter(), true);
    }

    public static void setCategoryBits(Fixture f, short bits) {
        Filter filter = new Filter();
        filter.categoryBits = bits;
        f.setFilterData(filter);
    }


    // Review the code below.
    public Body getB2Body() {
        return b2body.body;
    }

    public String getName() {
        return stats.name;
    }

    public int getHealth() {
        return stats.health;
    }


    public void setIsJumping(boolean jumping) {
        state.jumping = jumping;
    }

    public void setIsOnPlatform(boolean onPlatform) {
        state.onPlatform = onPlatform;
    }


    public void setLockedOnTarget(Character enemy) {
        targets.lockedOnTarget = enemy;
    }

    public void setInRangeTarget(Character enemy) {
        targets.inRangeTarget = enemy;
    }


    public AIActions getAIActions() {
        return AIActions;
    }

    public Queue<Actor> getDamageIndicators() {
        return damageIndicators;
    }

    @Override
    public void dispose() {
        sounds.sounds.values().forEach(s -> s.dispose());
    }

    @Override
    public String toString() {
        return stats.name;
    }

}