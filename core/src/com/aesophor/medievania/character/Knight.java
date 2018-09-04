package com.aesophor.medievania.character;

import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Knight extends Enemy implements Humanoid {
    
    private static final String TEXTURE_FILE = "Character/Knight/Knight.png";
    
    public Knight(AssetManager assets, World world, float x, float y) {
        super(assets.get(TEXTURE_FILE), world, x, y);

        name = "Castle guard";
        bodyWidth = 10;
        bodyHeight = 34;

        health = 100;
        movementSpeed = .20f;
        jumpHeight = 3.5f;
        attackForce = 1.2f;
        attackTime = 1.2f;
        attackRange = 14;
        attackDamage = 25;
        
        // Knight stand animation.
        idleAnimation = new TextureRegion(getTexture(), 8 * 42, 1 * 42, 42, 42);
        runAnimation = Utils.createAnimation(getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        jumpAnimation = Utils.createAnimation(getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        fallAnimation = Utils.createAnimation(getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        crouchAnimation = Utils.createAnimation(getTexture(), 12f / Constants.PPM, 0, 7, 0, 1 * 42, 42, 42);
        attackAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM, 0, 9, 0, 0, 42, 42);
        killedAnimation = Utils.createAnimation(getTexture(), 24f / Constants.PPM, 12, 19, 0, 1 * 42, 42, 42);
        
        // Sounds.
        footstepSound = assets.get("Sound/FX/Player/footstep.mp3");
        hurtSound = assets.get("Sound/FX/Player/hurt.wav");
        deathSound = assets.get("Sound/FX/Player/death.mp3");
        weaponSwingSound = assets.get("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        weaponHitSound = assets.get("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        jumpSound = assets.get("Sound/FX/Player/jump.wav", Sound.class);

        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.ENEMY;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PLAYER | CategoryBits.MELEE_WEAPON | CategoryBits.CLIFF_MARKER;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        short weaponMaskBits = CategoryBits.PLAYER | CategoryBits.OBJECT;

        defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);
        
        setBounds(0, 0, 50 / Constants.PPM, 50 / Constants.PPM);
        setRegion(idleAnimation);
        
        facingRight = false;
    }

}