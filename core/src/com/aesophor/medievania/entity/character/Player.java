package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.*;
import com.aesophor.medievania.component.sound.SoundType;
import com.aesophor.medievania.component.character.*;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.combat.CharacterKilledEvent;
import com.aesophor.medievania.event.combat.ItemPickedUpEvent;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Character {

    public Player(AssetManager assets, World world, float x, float y) {
        super(world, x, y);

        characterData = CharacterDataManager.getInstance().get("Player");
        stats = new StatsComponent(characterData.getStats());

        add(characterData);
        add(stats); // override stats.
        add(new PickupItemTargetComponent());
        add(new ControllableComponent());
        add(new PortalTargetComponent());
        add(new EquipmentSlotsComponent());
        add(new StatsRegenerationComponent(3f, 1, 10, 10));


        TextureAtlas atlas = assets.get(characterData.getAtlas());

        // Create animations by extracting frames from the spritesheet.
        Animation<TextureRegion> idleAnimation = Utils.createAnimation(atlas.findRegion("idle"), 22f / Constants.PPM, 0, 5, 0, 0 * 80, 80, 80);
        Animation<TextureRegion> runAnimation = Utils.createAnimation(atlas.findRegion("run"), 17f / Constants.PPM, 0, 7,  0, 0 * 80,  80, 80);
        Animation<TextureRegion> jumpAnimation = Utils.createAnimation(atlas.findRegion("jump"), 10f / Constants.PPM, 0, 3,  0, 0 * 80,  80, 80);
        Animation<TextureRegion> fallAnimation = Utils.createAnimation(atlas.findRegion("jump"), 10f / Constants.PPM, 4, 4,  0, 0 * 80,  80, 80);
        Animation<TextureRegion> crouchAnimation = Utils.createAnimation(atlas.findRegion("jump"), 10f / Constants.PPM, 5, 5,  0, 0 * 80,  80, 80);
        Animation<TextureRegion> attackAnimation = Utils.createAnimation(atlas.findRegion("attack"), 20f / Constants.PPM, 1, 6,  0, 0 * 80,  80, 80);
        Animation<TextureRegion> killedAnimation = Utils.createAnimation(atlas.findRegion("killed"), 30f / Constants.PPM, 0, 5,  0,      0,  80, 80);

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

        sprite.setBounds(0, 0, 115 / Constants.PPM, 110 / Constants.PPM);
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