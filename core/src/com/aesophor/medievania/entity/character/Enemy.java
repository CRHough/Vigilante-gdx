package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.sound.SoundType;
import com.aesophor.medievania.component.character.*;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Character {

    public Enemy(String enemyName, AssetManager assets, World world, float x, float y) {
        super(world, x, y);

        characterData = CharacterDataManager.getInstance().get(enemyName);
        stats = new StatsComponent(characterData.getStats());

        add(characterData);
        add(stats); // override stats.
        add(new DroppableItemsComponent(characterData.getItems()));
        add(new CharacterDataComponent());
        add(new CharacterAIComponent());

        TextureAtlas atlas = assets.get(characterData.getAtlas());

        // Modify this later.
        Animation<TextureRegion> idleAnimation = Utils.createAnimation(atlas.findRegion("idle"), 30f / Constants.PPM, 0, 3, 0 * 42, 0 * 42, 42, 44);
        Animation<TextureRegion> runAnimation = Utils.createAnimation(atlas.findRegion("run"), 24f / Constants.PPM, 0, 7, 0, 0 * 42, 42, 44);
        Animation<TextureRegion> jumpAnimation = Utils.createAnimation(atlas.findRegion("run"), 12f / Constants.PPM, 0, 7, 0, 0 * 42, 42, 44);
        Animation<TextureRegion> fallAnimation = Utils.createAnimation(atlas.findRegion("run"), 12f / Constants.PPM, 0, 7, 0, 0 * 42, 42, 44);
        Animation<TextureRegion> crouchAnimation = Utils.createAnimation(atlas.findRegion("run"), 12f / Constants.PPM, 0, 7, 0, 0 * 42, 42, 44);
        Animation<TextureRegion> attackAnimation = Utils.createAnimation(atlas.findRegion("attack"), 24f / Constants.PPM, 0, 9, 0, 0, 42, 44);
        Animation<TextureRegion> killedAnimation = Utils.createAnimation(atlas.findRegion("killed"), 32f / Constants.PPM, 0, 8, 0, 0 * 42, 42, 44);

        animations.put(State.IDLE, idleAnimation);
        animations.put(State.RUNNING, runAnimation);
        animations.put(State.JUMPING, jumpAnimation);
        animations.put(State.FALLING, fallAnimation);
        animations.put(State.CROUCHING, crouchAnimation);
        animations.put(State.ATTACKING, attackAnimation);
        animations.put(State.KILLED, killedAnimation);


        // Sounds.
        //Sound footstepSound = assets.getDroppableItems("sfx/player/footstep.mp3");
        Sound hurtSound = assets.get("sfx/player/hurt.wav");
        Sound deathSound = assets.get("sfx/player/death.mp3");
        Sound weaponSwingSound = assets.get("sfx/player/weapon_swing.ogg", Sound.class);
        Sound weaponHitSound = assets.get("sfx/player/weapon_hit.ogg", Sound.class);
        Sound jumpSound = assets.get("sfx/player/jump.wav", Sound.class);

        //sounds.put(SoundType.FOOTSTEP, footstepSound);
        sounds.put(SoundType.JUMP, jumpSound);
        sounds.put(SoundType.HURT, hurtSound);
        sounds.put(SoundType.DEATH, deathSound);
        sounds.put(SoundType.WEAPON_SWING, weaponSwingSound);
        sounds.put(SoundType.WEAPON_HIT, weaponHitSound);

        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.ENEMY;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PLAYER | CategoryBits.MELEE_WEAPON | CategoryBits.CLIFF_MARKER;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        short weaponMaskBits = CategoryBits.PLAYER | CategoryBits.OBJECT;

        defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);

        sprite.setBounds(0, 0, 50 / Constants.PPM, 50 / Constants.PPM);
        state.setFacingRight(false);
    }


    @Override
    public void receiveDamage(Character source, int damage) {
        super.receiveDamage(source, damage);
        state.setAlerted(true);
    }

}