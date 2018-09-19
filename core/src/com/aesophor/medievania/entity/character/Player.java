package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.*;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.combat.CharacterKilledEvent;
import com.aesophor.medievania.event.combat.ItemPickedUpEvent;
import com.aesophor.medievania.map.Portal;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Character {

    private static final String TEXTURE_FILE = "character/bandit/Bandit.png";

    private PickupItemTargetComponent pickupItemTargetComponent;
    private Portal currentPortal;

    public Player(AssetManager assets, World world, float x, float y) {
        super(assets.get(TEXTURE_FILE), world, x, y);

        pickupItemTargetComponent = new PickupItemTargetComponent();

        add(pickupItemTargetComponent);
        add(new ControllableComponent());
        add(new EquipmentSlotsComponent());
        add(new StatsRegenerationComponent(3f, 1, 10, 10));

        stats.setName("Michael");
        stats.setBodyWidth(10);
        stats.setBodyHeight(34);

        stats.modFullHealth(100);
        stats.modFullStamina(100);
        stats.modHealth(100);
        stats.modStamina(100);

        stats.setMovementSpeed(.3f);
        stats.setJumpHeight(3f);
        stats.setAttackForce(1f);
        stats.setAttackTime(1.8f);
        stats.setAttackRange(15);
        stats.setAttackDamage(25);

        // Create animations by extracting frames from the spritesheet.
        Animation<TextureRegion> idleAnimation = Utils.createAnimation(sprite.getTexture(), 10f / Constants.PPM, 0, 0, 7 * 80, 2 * 80, 80, 80);
        Animation<TextureRegion> runAnimation = Utils.createAnimation(sprite.getTexture(), 20f / Constants.PPM, 0, 7,  0, 3 * 80,  80, 80);
        Animation<TextureRegion> jumpAnimation = Utils.createAnimation(sprite.getTexture(), 10f / Constants.PPM, 0, 3,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> fallAnimation = Utils.createAnimation(sprite.getTexture(), 10f / Constants.PPM, 4, 4,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> crouchAnimation = Utils.createAnimation(sprite.getTexture(), 10f / Constants.PPM, 5, 5,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> attackAnimation = Utils.createAnimation(sprite.getTexture(), 20f / Constants.PPM, 1, 6,  0, 2 * 80,  80, 80);
        Animation<TextureRegion> killedAnimation = Utils.createAnimation(sprite.getTexture(), 30f / Constants.PPM, 0, 5,  0,      0,  80, 80);

        animations.put(State.IDLE, idleAnimation);
        animations.put(State.RUNNING, runAnimation);
        animations.put(State.JUMPING, jumpAnimation);
        animations.put(State.FALLING, fallAnimation);
        animations.put(State.CROUCHING, crouchAnimation);
        animations.put(State.ATTACKING, attackAnimation);
        animations.put(State.KILLED, killedAnimation);


        // Sounds.
        //Sound footstepSound = gameWorldManager.getAssets().getDroppableItems("sfx/player/footstep.mp3");
        Sound hurtSound = assets.get("sfx/player/hurt.wav");
        Sound deathSound = assets.get("sfx/player/death.mp3");
        Sound weaponSwingSound = assets.get("sfx/player/weapon_swing.ogg", Sound.class);
        Sound weaponHitSound = assets.get("sfx/player/weapon_hit.ogg", Sound.class);
        Sound jumpSound = assets.get("sfx/player/jump.wav", Sound.class);
        Sound itemPickedupSound = assets.get("sfx/player/pickup_item.mp3", Sound.class);

        //sounds.put(SoundType.FOOTSTEP, footstepSound);
        sounds.put(SoundType.JUMP, jumpSound);
        sounds.put(SoundType.HURT, hurtSound);
        sounds.put(SoundType.DEATH, deathSound);
        sounds.put(SoundType.WEAPON_SWING, weaponSwingSound);
        sounds.put(SoundType.WEAPON_HIT, weaponHitSound);
        sounds.put(SoundType.ITEM_PICKEDUP, itemPickedupSound);


        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.PLAYER;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PORTAL | CategoryBits.ENEMY | CategoryBits.MELEE_WEAPON | CategoryBits.ITEM;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        short weaponMaskBits = CategoryBits.ENEMY | CategoryBits.OBJECT;
        super.defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);

        sprite.setBounds(0, 0, 120 / Constants.PPM, 120 / Constants.PPM);
    }

    public Portal getCurrentPortal() {
        return currentPortal;
    }

    public void setCurrentPortal(Portal currentPortal) {
        this.currentPortal = currentPortal;
    }

    public PickupItemTargetComponent getPickupItemTargetComponent() {
        return pickupItemTargetComponent;
    }

    public void pickup(Item item) {
        inventory.get(item.getType()).add(item);
        world.destroyBody(Mappers.B2BODY.get(item).getBody());
        GameEventManager.getInstance().fireEvent(new ItemPickedUpEvent(item));
        System.out.println(inventory);
        sounds.get(SoundType.ITEM_PICKEDUP).play();
    }

    public void reposition(Vector2 position) {
        b2body.getBody().setTransform(position, 0);
    }

    public void reposition(float x, float y) {
        b2body.getBody().setTransform(x, y, 0);
    }

    @Override
    public void inflictDamage(Character target, int damage) {
        super.inflictDamage(target, damage);

        if (Mappers.STATE.get(target).isSetToKill()) {
            GameEventManager.getInstance().fireEvent(new CharacterKilledEvent(this, target));
        }
    }

    @Override
    public void receiveDamage(Character source, int damage) {
        super.receiveDamage(source, damage);
        state.setInvincible(true);

        Timer.schedule(new Task() {
            @Override
            public void run() {
                if (!state.isSetToKill()) {
                    state.setInvincible(false);
                }
            }
        }, 3f);
    }

}