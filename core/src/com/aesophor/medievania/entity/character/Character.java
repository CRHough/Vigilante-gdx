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
import com.aesophor.medievania.util.Utils;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import java.util.Arrays;

public abstract class Character extends Entity implements Disposable {

    private final AIActions AIActions;

    public Character(String name, AssetManager assets, World world, float x, float y) {
        AIActions = new AIActions(this);

        CharacterDataComponent characterData = CharacterDataManager.getInstance().get(name);
        AnimationComponent animations = new AnimationComponent();
        SoundComponent sounds = new SoundComponent();

        add(characterData);
        add(animations);
        add(sounds);
        add(new StatsComponent(characterData.getStats()));
        add(new B2BodyComponent(world));
        add(new SpriteComponent(x * Constants.PPM, y * Constants.PPM));
        add(new StateComponent(State.IDLE));
        add(new CombatTargetComponent());
        add(new InventoryComponent());
        add(new EquipmentSlotsComponent());
        add(new StatsRegenerationComponent(characterData.getStatsRegen()));

        // Initialize animations by extracting frames from the texture atlas.
        TextureAtlas atlas = assets.get(characterData.getAtlas());
        Arrays.stream(State.values()).forEach(s -> animations.put(s, Utils.createAnimation(atlas, characterData, s.name(), Constants.PPM)));

        // Initialize sounds.
        Arrays.stream(SoundType.values()).forEach(s -> sounds.put(s, assets.get(characterData.getSoundData().get(s.name()))));
    }


    protected void defineBody(BodyDef.BodyType type, short bodyCategoryBits, short bodyMaskBits, short feetMaskBits, short meleeWeaponMaskBits) {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        SpriteComponent sprite = Mappers.SPRITE.get(this);

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
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StatsComponent stats = Mappers.STATS.get(this);

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
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StatsComponent stats = Mappers.STATS.get(this);

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
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StatsComponent stats = Mappers.STATS.get(this);

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
        InventoryComponent inventory = Mappers.INVENTORY.get(this);
        EquipmentType equipmentType = Mappers.EQUIPMENT_DATA.get(item).getType();

        EquipmentSlotsComponent equipmentSlots = Mappers.EQUIPMENT_SLOTS.get(this);
        if (equipmentSlots.has(equipmentType)) {
            equipmentSlots.unequip(item);
            inventory.add(item);
            GameEventManager.getInstance().fireEvent(new ItemUnequippedEvent(this, item));
        }
    }


    public void moveLeft() {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);

        state.setFacingRight(false);
        if (b2body.getBody().getLinearVelocity().x >= -stats.getMovementSpeed() * 2) {
            b2body.getBody().applyLinearImpulse(new Vector2(-stats.getMovementSpeed(), 0), b2body.getBody().getWorldCenter(), true);
        }
    }

    public void moveRight() {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);

        state.setFacingRight(true);
        if (b2body.getBody().getLinearVelocity().x <= stats.getMovementSpeed() * 2) {
            b2body.getBody().applyLinearImpulse(new Vector2(stats.getMovementSpeed(), 0), b2body.getBody().getWorldCenter(), true);
        }
    }

    public void forwardRush() {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);

        if (b2body.getBody().getLinearVelocity().x <= stats.getMovementSpeed() * 2 && b2body.getBody().getLinearVelocity().x >= -stats.getMovementSpeed() * 2) {
            float rushForce = (state.facingRight()) ? stats.getMovementSpeed() * 5 : -stats.getMovementSpeed() * 5;
            b2body.getBody().applyLinearImpulse(new Vector2(rushForce, 0), b2body.getBody().getWorldCenter(), true);
        }
    }

    public void jump() {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);
        SoundComponent sounds = Mappers.SOUNDS.get(this);

        if (!state.isJumping()) {
            state.setJumping(true);

            b2body.getBody().applyLinearImpulse(new Vector2(0, stats.getJumpHeight()), b2body.getBody().getWorldCenter(), true);
            sounds.get(SoundType.JUMP).play();
        }
    }

    public void jumpDown() {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);

        if (state.isOnPlatform()) {
            state.setOnPlatform(false);
            b2body.getBody().setTransform(b2body.getBody().getPosition().x, b2body.getBody().getPosition().y - 8f / Constants.PPM, 0);
        }
    }

    public void crouch() {
        StateComponent state = Mappers.STATE.get(this);

        if (!state.isCrouching()) {
            state.setCrouching(true);
        }
    }

    public void getUp() {
        StateComponent state = Mappers.STATE.get(this);

        if (state.isCrouching()) {
            state.setCrouching(false);
        }
    }


    public void swingWeapon() {
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);
        CombatTargetComponent targets = Mappers.COMBAT_TARGETS.get(this);
        SoundComponent sounds = Mappers.SOUNDS.get(this);

        if (!state.isAttacking()) {
            state.setAttacking(true);

            stats.modStamina(-10);

            // A character can have multiple inRangeTargets which are stored as an array.
            // When an inRangeTarget dies, the target will be removed from the array.
            if (targets.hasInRangeTarget()) {
                Character currentTarget = targets.getInRangeTargets().first();
                StateComponent targetState = Mappers.STATE.get(currentTarget);
                CombatTargetComponent targetsTarget = Mappers.COMBAT_TARGETS.get(currentTarget); // lol...

                if (!targetState.isInvincible() && !targetState.isSetToKill()) {
                    targets.setLockedOnTarget(targets.getInRangeTargets().first());
                    targetsTarget.setLockedOnTarget(this);

                    inflictDamage(targets.getInRangeTargets().first(), stats.getBasePhysicalDamage());

                    if (targets.getInRangeTargets().first().getComponent(StateComponent.class).isSetToKill()) {
                        targets.getInRangeTargets().removeValue(targets.getInRangeTargets().first(), false);
                    }

                    sounds.get(SoundType.WEAPON_HIT).play();
                }
            }

            sounds.get(SoundType.WEAPON_SWING).play();
            return;
        }
    }

    public void inflictDamage(Character target, int damage) {
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);

        target.receiveDamage(this, damage);
        target.knockedBack((state.facingRight()) ? stats.getAttackForce() : -stats.getAttackForce());
    }

    public void receiveDamage(Character source, int damage) {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);
        SoundComponent sounds = Mappers.SOUNDS.get(this);

        if (!state.isInvincible()) {
            stats.modHealth(-damage);

            GameEventManager.getInstance().fireEvent(new InflictDamageEvent(source, this, damage));

            if (stats.getHealth() == 0) {
                setCategoryBits(b2body.getBodyFixture(), CategoryBits.DESTROYED);
                state.setSetToKill(true);
                sounds.get(SoundType.KILLED).play();
            } else {
                sounds.get(SoundType.HURT).play();
            }
        }
    }

    public void knockedBack(float force) {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        b2body.getBody().applyLinearImpulse(new Vector2(force, 1f), b2body.getBody().getWorldCenter(), true);
    }

    public static void setCategoryBits(Fixture f, short bits) {
        Filter filter = new Filter();
        filter.categoryBits = bits;
        f.setFilterData(filter);
    }


    public AIActions getAIActions() {
        return AIActions;
    }

    @Override
    public void dispose() {
        Mappers.SPRITE.get(this).dispose();
        Mappers.SOUNDS.get(this).values().forEach(Sound::dispose);
    }

    @Override
    public String toString() {
        return Mappers.STATS.get(this).getName();
    }

}