package com.aesophor.medievania.character;

import com.aesophor.medievania.component.State;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Knight extends Enemy implements Humanoid {
    
    private static final String TEXTURE_FILE = "character/knight/Knight.png";
    
    public Knight(AssetManager assets, World world, float x, float y) {
        super(assets.get(TEXTURE_FILE), world, x, y);

        cc.name = "Castle guard";
        cc.bodyWidth = 10;
        cc.bodyHeight = 34;

        cc.health = 100;
        cc.movementSpeed = .20f;
        cc.jumpHeight = 3.5f;
        cc.attackForce = 1.2f;
        cc.attackTime = 1.2f;
        cc.attackRange = 14;
        cc.attackDamage = 25;
        
        // Knight stand animation.
        Animation<TextureRegion> idleAnimation = Utils.createAnimation(spc.sprite.getTexture(), 10f / Constants.PPM, 0, 0, 8 * 42, 1 * 42, 42, 42);
        Animation<TextureRegion> runAnimation = Utils.createAnimation(spc.sprite.getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        Animation<TextureRegion> jumpAnimation = Utils.createAnimation(spc.sprite.getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        Animation<TextureRegion> fallAnimation = Utils.createAnimation(spc.sprite.getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        Animation<TextureRegion> crouchAnimation = Utils.createAnimation(spc.sprite.getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        Animation<TextureRegion> attackAnimation = Utils.createAnimation(spc.sprite.getTexture(), 10f / Constants.PPM, 0, 9, 0, 0, 42, 42);
        Animation<TextureRegion> killedAnimation = Utils.createAnimation(spc.sprite.getTexture(), 24f / Constants.PPM, 12, 19, 0, 1 * 42, 42, 42);

        ac.animations.put(State.IDLE, idleAnimation);
        ac.animations.put(State.RUNNING, runAnimation);
        ac.animations.put(State.JUMPING, jumpAnimation);
        ac.animations.put(State.FALLING, fallAnimation);
        ac.animations.put(State.CROUCHING, crouchAnimation);
        ac.animations.put(State.ATTACKING, attackAnimation);
        ac.animations.put(State.KILLED, killedAnimation);


        // Sounds.
        footstepSound = assets.get("sfx/player/footstep.mp3");
        hurtSound = assets.get("sfx/player/hurt.wav");
        deathSound = assets.get("sfx/player/death.mp3");
        weaponSwingSound = assets.get("sfx/player/weapon_swing.ogg", Sound.class);
        weaponHitSound = assets.get("sfx/player/weapon_hit.ogg", Sound.class);
        jumpSound = assets.get("sfx/player/jump.wav", Sound.class);

        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.ENEMY;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PLAYER | CategoryBits.MELEE_WEAPON | CategoryBits.CLIFF_MARKER;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        short weaponMaskBits = CategoryBits.PLAYER | CategoryBits.OBJECT;

        defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);

        spc.sprite.setBounds(0, 0, 50 / Constants.PPM, 50 / Constants.PPM);
        stc.facingRight = false;
    }

}