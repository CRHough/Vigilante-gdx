package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.*;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.component.graphics.AnimationComponent;
import com.aesophor.medievania.component.graphics.SpriteComponent;
import com.aesophor.medievania.component.physics.B2BodyComponent;
import com.aesophor.medievania.component.sound.SoundComponent;
import com.aesophor.medievania.component.sound.SoundType;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.character.ItemEquippedEvent;
import com.aesophor.medievania.event.character.ItemUnequippedEvent;
import com.aesophor.medievania.event.combat.InflictDamageEvent;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

public abstract class Character extends Entity implements Disposable {

    protected SpriteComponent sprite;
    protected AnimationComponent animations;
    protected B2BodyComponent b2body;
    protected StateComponent state;
    protected SoundComponent sounds;
    protected StatsComponent stats;
    protected CombatTargetComponent targets;
    protected InventoryComponent inventory;
    protected CharacterDataComponent characterData;

    protected AIActions AIActions;
    protected World world;

    public Character(World world, float x, float y) {
        this.world = world;

        stats = new StatsComponent();
        animations = new AnimationComponent();
        b2body = new B2BodyComponent(world);
        sprite = new SpriteComponent(x * Constants.PPM, y * Constants.PPM);
        state = new StateComponent(State.IDLE);
        sounds = new SoundComponent();
        targets = new CombatTargetComponent();
        inventory = new InventoryComponent();

        add(stats);
        add(animations);
        add(b2body);
        add(sprite);
        add(state);
        add(targets);
        add(inventory);
        add(new EquipmentSlotsComponent());

        AIActions = new AIActions(this);
    }


    protected void defineBody(BodyDef.BodyType type, short bodyCategoryBits, short bodyMaskBits, short feetMaskBits, short meleeWeaponMaskBits) {
        Body body = b2body.getBodyBuilder().type(type)
                .position(sprite.getX(), sprite.getY(), Constants.PPM)
                .buildBody();

        b2body.setBody(body);
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
        Fixture bodyFixture = b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(), stats.getBodyWidth() / 2, stats.getBodyHeight() / 2, Constants.PPM)
                .categoryBits(categoryBits)
                .maskBits(maskBits)
                .setUserData(this)
                .buildFixture();

        b2body.setBodyFixture(bodyFixture);
    }

    /**
     * Builds feet fixture which is a bit lower than the body fixture.
     * @param maskBits defines which objects the feet fixture can collide with.
     */
    protected void createFeetFixture(short maskBits) {
        Vector2[] feetPolyVertices = new Vector2[4];
        feetPolyVertices[0] = new Vector2(-stats.getBodyWidth() / 2 + 1, -stats.getBodyHeight() / 2);
        feetPolyVertices[1] = new Vector2(stats.getBodyWidth() / 2 - 1, -stats.getBodyHeight() / 2);
        feetPolyVertices[2] = new Vector2(-stats.getBodyWidth() / 2 + 1, -stats.getBodyHeight() / 2 - 1);
        feetPolyVertices[3] = new Vector2(stats.getBodyWidth() / 2 - 1, -stats.getBodyHeight() / 2 - 1);

        Fixture feetFixture = b2body.getBodyBuilder().newPolygonFixture(feetPolyVertices, Constants.PPM)
                .categoryBits(CategoryBits.FEET)
                .maskBits(maskBits)
                .isSensor(true)
                .setUserData(this)
                .buildFixture();

        b2body.setFeetFixture(feetFixture);
    }

    protected void createMeleeWeaponFixture(short maskBits) {
        Vector2 meleeAttackFixturePosition = new Vector2(stats.getAttackRange(), 0);

        Fixture meleeWeaponFixture = b2body.getBodyBuilder().newCircleFixture(meleeAttackFixturePosition, stats.getAttackRange(), Constants.PPM)
                .categoryBits(CategoryBits.MELEE_WEAPON)
                .maskBits(maskBits)
                .isSensor(true)
                .setUserData(this)
                .buildFixture();

        b2body.setMeleeWeaponFixture(meleeWeaponFixture);
    }


    public void equip(Item item) {
        InventoryComponent inventory = Mappers.INVENTORY.get(this);
        EquipmentSlotsComponent equipmentSlots = Mappers.EQUIPMENT_SLOTS.get(this);
        EquipmentType equipmentType = Mappers.EQUIPMENT_DATA.get(item).getType();

        // If this equipment slot has already been occupied, add the previously item back to inventory first.
        if (equipmentSlots.has(equipmentType)) {
            inventory.add(equipmentSlots.get(equipmentType));
            GameEventManager.getInstance().fireEvent(new ItemUnequippedEvent(this, equipmentSlots.get(equipmentType)));
        }

        // Equip the new item.
        inventory.remove(item);
        equipmentSlots.equip(item);
        GameEventManager.getInstance().fireEvent(new ItemEquippedEvent(this, item));
    }

    public void unequip(Item item) {
        EquipmentType equipmentType = Mappers.EQUIPMENT_DATA.get(item).getType();

        EquipmentSlotsComponent equipmentSlots = Mappers.EQUIPMENT_SLOTS.get(this);
        if (equipmentSlots.has(equipmentType)) {
            equipmentSlots.unequip(item);
            inventory.add(item);
            GameEventManager.getInstance().fireEvent(new ItemUnequippedEvent(this, item));
        }
    }


    public void moveLeft() {
        state.setFacingRight(false);

        if (b2body.getBody().getLinearVelocity().x >= -stats.getMovementSpeed() * 2) {
            b2body.getBody().applyLinearImpulse(new Vector2(-stats.getMovementSpeed(), 0), b2body.getBody().getWorldCenter(), true);
        }
    }

    public void moveRight() {
        state.setFacingRight(true);

        if (b2body.getBody().getLinearVelocity().x <= stats.getMovementSpeed() * 2) {
            b2body.getBody().applyLinearImpulse(new Vector2(stats.getMovementSpeed(), 0), b2body.getBody().getWorldCenter(), true);
        }
    }

    public void forwardRush() {
        if (b2body.getBody().getLinearVelocity().x <= stats.getMovementSpeed() * 2 && b2body.getBody().getLinearVelocity().x >= -stats.getMovementSpeed() * 2) {
            float rushForce = (state.facingRight()) ? stats.getMovementSpeed() * 5 : -stats.getMovementSpeed() * 5;
            b2body.getBody().applyLinearImpulse(new Vector2(rushForce, 0), b2body.getBody().getWorldCenter(), true);
        }
    }

    public void jump() {
        if (!state.isJumping()) {
            state.setJumping(true);

            getB2Body().applyLinearImpulse(new Vector2(0, stats.getJumpHeight()), b2body.getBody().getWorldCenter(), true);
            sounds.get(SoundType.JUMP).play();
        }
    }

    public void jumpDown() {
        if (state.isOnPlatform()) {
            state.setOnPlatform(false);
            b2body.getBody().setTransform(b2body.getBody().getPosition().x, b2body.getBody().getPosition().y - 8f / Constants.PPM, 0);
        }
    }

    public void crouch() {
        if (!state.isCrouching()) {
            state.setCrouching(true);
        }
    }

    public void getUp() {
        if (state.isCrouching()) {
            state.setCrouching(false);
        }
    }


    public void swingWeapon() {
        if (!state.isAttacking()) {
            state.setAttacking(true);

            stats.modStamina(-10);

            // A character can have multiple inRangeTargets which are stored as an array.
            // When an inRangeTarget dies, the target will be removed from the array.
            if (targets.hasInRangeTarget() && !targets.getInRangeTargets().first().state.isInvincible() && !targets.getInRangeTargets().first().state.isSetToKill()) {
                setLockedOnTarget(targets.getInRangeTargets().first());
                targets.getInRangeTargets().first().setLockedOnTarget(this);

                inflictDamage(targets.getInRangeTargets().first(), stats.getBasePhysicalDamage());

                if (targets.getInRangeTargets().first().getComponent(StateComponent.class).isSetToKill()) {
                    targets.getInRangeTargets().removeValue(targets.getInRangeTargets().first(), false);
                }

                sounds.get(SoundType.WEAPON_HIT).play();
            }

            sounds.get(SoundType.WEAPON_SWING).play();
            return;
        }
    }

    public void inflictDamage(Character target, int damage) {
        target.receiveDamage(this, damage);
        target.knockedBack((state.facingRight()) ? stats.getAttackForce() : -stats.getAttackForce());
    }

    public void receiveDamage(Character source, int damage) {
        if (!state.isInvincible()) {
            stats.modHealth(-damage);

            GameEventManager.getInstance().fireEvent(new InflictDamageEvent(source, this, damage));

            if (stats.getHealth() == 0) {
                setCategoryBits(b2body.getBodyFixture(), CategoryBits.DESTROYED);
                state.setSetToKill(true);
                sounds.get(SoundType.DEATH).play();
            } else {
                sounds.get(SoundType.HURT).play();
            }
        }
    }

    public void knockedBack(float force) {
        b2body.getBody().applyLinearImpulse(new Vector2(force, 1f), b2body.getBody().getWorldCenter(), true);
    }

    public static void setCategoryBits(Fixture f, short bits) {
        Filter filter = new Filter();
        filter.categoryBits = bits;
        f.setFilterData(filter);
    }


    // Review the code below.
    public Body getB2Body() {
        return b2body.getBody();
    }

    public int getHealth() {
        return stats.getHealth();
    }

    public int getStamina() {
        return stats.getStamina();
    }


    public void setIsJumping(boolean jumping) {
        state.setJumping(jumping);
    }

    public void setIsOnPlatform(boolean onPlatform) {
        state.setOnPlatform(onPlatform);
    }


    public void setLockedOnTarget(Character enemy) {
        targets.setLockedOnTarget(enemy);
    }

    public void addInRangeTarget(Character enemy) {
        targets.getInRangeTargets().add(enemy);
    }

    public void removeInRangeTarget(Character enemy) {
        targets.getInRangeTargets().removeValue(enemy, false);
    }


    public AIActions getAIActions() {
        return AIActions;
    }

    @Override
    public void dispose() {
        sprite.dispose();
        sounds.values().forEach(s -> s.dispose());
    }

    @Override
    public String toString() {
        return stats.getName();
    }

}