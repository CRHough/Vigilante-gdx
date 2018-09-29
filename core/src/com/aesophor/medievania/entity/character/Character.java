package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.GameAssetManager;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.*;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.component.graphics.SpriteComponent;
import com.aesophor.medievania.component.physics.B2BodyComponent;
import com.aesophor.medievania.component.sound.SoundComponent;
import com.aesophor.medievania.component.sound.SoundType;
import com.aesophor.medievania.entity.data.CharacterDataManager;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.character.ItemDiscardedEvent;
import com.aesophor.medievania.event.character.ItemEquippedEvent;
import com.aesophor.medievania.event.character.ItemPickedUpEvent;
import com.aesophor.medievania.event.character.ItemUnequippedEvent;
import com.aesophor.medievania.event.combat.InflictDamageEvent;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import java.util.Arrays;

public abstract class Character extends Entity implements Disposable {

    private final AssetManager assets;
    private final AIActions AIActions;

    public Character(String name, AssetManager assets, World world, float x, float y) {
        this.assets = assets;
        AIActions = new AIActions(this);

        CharacterDataComponent characterData = CharacterDataManager.getInstance().get(name);
        CharacterAnimationComponent animations = new CharacterAnimationComponent();
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
        Arrays.stream(State.values()).forEach(s -> {
            animations.put(s, Utils.createAnimation(atlas, characterData, s.name(), Constants.PPM));
        });

        // Initialize sounds.
        Arrays.stream(SoundType.values()).forEach(s -> {
            sounds.put(s, assets.get(characterData.getSoundData().get(s.name())));
        });
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
        feetPolyVertices[0] = new Vector2(-stats.getBodyWidth() / 2 + 1, 0);
        feetPolyVertices[1] = new Vector2(stats.getBodyWidth() / 2 - 1, 0);
        feetPolyVertices[2] = new Vector2(-stats.getBodyWidth() / 2 + 1, -stats.getBodyHeight() / 2 - 1);
        feetPolyVertices[3] = new Vector2(stats.getBodyWidth() / 2 - 1, -stats.getBodyHeight() / 2 - 1);

        Fixture feetFixture = b2body.getBodyBuilder().newPolygonFixture(feetPolyVertices, Constants.PPM)
                .categoryBits(CategoryBits.FEET)
                .maskBits(maskBits)
                .setUserData(this)
                .buildFixture();

        b2body.setFeetFixture(feetFixture);
    }

    /**
     * Builds melee weapon fixture. When the body of an enemy collides with this fixture,
     * the enemy will be added to player's inRangeTarget.
     * (See CombatTargetComponent.java and WorldContactListener.java)
     * @param maskBits defines which objects the melee weapon fixture can collide with.
     */
    protected void createMeleeWeaponFixture(short maskBits) {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StatsComponent stats = Mappers.STATS.get(this);

        Vector2 meleeAttackFixturePosition = new Vector2(stats.getAttackRange(), 0);

        Fixture meleeWeaponFixture = b2body.getBodyBuilder().newCircleFixture(meleeAttackFixturePosition, stats.getAttackRange(), Constants.PPM)
                .categoryBits(CategoryBits.MELEE_WEAPON)
                .maskBits(maskBits)
                .setSensor(true)
                .setUserData(this)
                .buildFixture();

        b2body.setMeleeWeaponFixture(meleeWeaponFixture);
    }


    /**
     * Removes the specified item from character's inventory and equips the specified item.
     * If there's already an item in the target equipment slot, the item in that slot will
     * be unequipped first before the new item is equipped.
     * @param item item to equip.
     */
    public void equip(Item item) {
        InventoryComponent inventory = Mappers.INVENTORY.get(this);
        EquipmentSlotsComponent equipmentSlots = Mappers.EQUIPMENT_SLOTS.get(this);
        EquipmentType equipmentType = Mappers.EQUIPMENT_DATA.get(item).getType();
        SoundComponent sounds = Mappers.SOUNDS.get(this);

        // If this equipment slot has already been occupied, add the previously item back to inventory first.
        unequip(equipmentType);

        // Equip the new item.
        inventory.remove(item);
        equipmentSlots.put(item);
        GameEventManager.getInstance().fireEvent(new ItemEquippedEvent(this, item));
        sounds.get(SoundType.EQUIPMENT_CHANGED).play();
    }

    /**
     * Removes the equipped item of the specified equipment type and add it to character's inventory.
     * Nothing happens if the slot is already empty.
     * @param equipmentType equipment type of item to unequip.
     */
    public void unequip(EquipmentType equipmentType) {
        InventoryComponent inventory = Mappers.INVENTORY.get(this);
        EquipmentSlotsComponent equipmentSlots = Mappers.EQUIPMENT_SLOTS.get(this);
        SoundComponent sounds = Mappers.SOUNDS.get(this);

        if (equipmentSlots.has(equipmentType)) {
            Item item = equipmentSlots.get(equipmentType);
            equipmentSlots.remove(item);
            inventory.add(item);
            GameEventManager.getInstance().fireEvent(new ItemUnequippedEvent(this, item));
            sounds.get(SoundType.EQUIPMENT_CHANGED).play();
        }
    }

    /**
     * Picks up the specified item and add it to character's inventory.
     * @param item item to pick up.
     */
    public void pickup(Item item) {
        InventoryComponent inventory = Mappers.INVENTORY.get(this);
        SoundComponent sounds = Mappers.SOUNDS.get(this);

        inventory.add(item);
        GameEventManager.getInstance().fireEvent(new ItemPickedUpEvent(item));

        sounds.get(SoundType.ITEM_PICKEDUP).play();
    }

    /**
     * Discards the specified item and drop it in the world.
     * @param item item to discard.
     */
    public void discard(Item item) {
        Mappers.INVENTORY.get(this).remove(item);
        GameEventManager.getInstance().fireEvent(new ItemDiscardedEvent(item)); // move these events firing to Player?
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

    public void jump() {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);
        SoundComponent sounds = Mappers.SOUNDS.get(this);

        if (!state.isJumping()) {
            state.setJumping(true);

            b2body.getBody().applyLinearImpulse(new Vector2(0, stats.getJumpHeight()), b2body.getBody().getWorldCenter(), true);
            //sounds.get(SoundType.JUMP).play();
        }
    }

    public void jumpDown() {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);

        if (state.isOnPlatform()) {
            state.setOnPlatform(false);
            b2body.getFeetFixture().setSensor(true);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    b2body.getFeetFixture().setSensor(false);
                }
            }, .1f);
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
        target.knockedBack((state.isFacingRight()) ? stats.getAttackForce() : -stats.getAttackForce());
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


    // ---------- Clean up this part later! ----------
    public void batPower() {
        B2BodyComponent b2body = Mappers.B2BODY.get(this);
        StateComponent state = Mappers.STATE.get(this);
        StatsComponent stats = Mappers.STATS.get(this);
        SpriteComponent sprite = Mappers.SPRITE.get(this);

        if (b2body.getBody().getLinearVelocity().x <= stats.getMovementSpeed() * 2 && b2body.getBody().getLinearVelocity().x >= -stats.getMovementSpeed() * 2) {
            float rushForce = (state.isFacingRight()) ? stats.getMovementSpeed() * 12 : -stats.getMovementSpeed() * 12;
            b2body.getBodyFixture().setSensor(true);

            CharacterAnimationComponent animations = getComponent(CharacterAnimationComponent.class);

            Texture texture = assets.get(GameAssetManager.TEXTURE_BAT);
            animations.put(State.SKILL, Utils.createAnimation(texture, 12f / Constants.PPM, 0, 2, 0, 0, 42, 42));
            sprite.setBounds(0, 0, 56f / 100, 56f / 100);
            state.setUsingSkill(true);

            b2body.getBody().applyLinearImpulse(new Vector2(rushForce, 0), b2body.getBody().getWorldCenter(), true);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    b2body.getBodyFixture().setSensor(false);
                    sprite.setBounds(0, 0, 105f / 100, 105f / 100);
                    state.setUsingSkill(false);
                    assets.unload(GameAssetManager.TEXTURE_BAT);
                }
            }, .5f);
        }
    }
    // -----------------------------------------------


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
        // use unload()
        Mappers.SPRITE.get(this).dispose();
        //Mappers.SOUNDS.get(this).values().forEach(Sound::dispose);
    }

    @Override
    public String toString() {
        return Mappers.STATS.get(this).getName();
    }

}