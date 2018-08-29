package com.aesophor.medievania.character;

import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Rumble;
import com.aesophor.medievania.util.Utils;
import com.aesophor.medievania.util.CategoryBits;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Character implements Humanoid, Controllable {
    
    private static final String TEXTURE_FILE = "Character/Bandit/Bandit.png";
    private boolean isAtPortal;
    
    public Player(AssetManager assets, World world, float x, float y) {
        super(assets.get(TEXTURE_FILE), world, x, y);

        bodyWidth = 10;
        bodyHeight = 34;

        health = 100;
        movementSpeed = .25f;
        jumpHeight = 3f;
        attackForce = 1f;
        attackTime = 1f;
        attackRange = 15;
        attackDamage = 25;
        
        // Create animations by extracting frames from the spritesheet.
        idleAnimation = new TextureRegion(getTexture(), 7 * 80, 2 * 80, 80, 80);
        runAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,     0, 7,  0, 3 * 80,  80, 80);
        jumpAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,     0, 3,  0, 1 * 80,  80, 80);
        fallAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,    4, 4,  0, 1 * 80,  80, 80);
        crouchAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,  5, 5,  0, 1 * 80,  80, 80);
        attackAnimation = Utils.createAnimation(getTexture(), 12f / Constants.PPM,  1, 6,  0, 2 * 80,  80, 80);
        killedAnimation = Utils.createAnimation(getTexture(), 24f / Constants.PPM,  0, 5,  0,      0,  80, 80);
        
        // Sounds.
        footstepSound = assets.get("Sound/FX/Player/footstep.mp3");
        hurtSound = assets.get("Sound/FX/Player/hurt.wav");
        deathSound = assets.get("Sound/FX/Player/death.mp3");
        weaponSwingSound = assets.get("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        weaponHitSound = assets.get("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        jumpSound = assets.get("Sound/FX/Player/jump.wav", Sound.class);
        
        defineBody();
        
        setBounds(0, 0, 120 / Constants.PPM, 120 / Constants.PPM);
        setRegion(idleAnimation);
    }
    
    
    @Override
    public void defineBody() {
        super.defineBody();

        short bodyCategoryBits = CategoryBits.PLAYER;
        short bodyMaskBits = (short) CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.ENEMY | CategoryBits.MELEE_WEAPON;
        short weaponMaskBits = (short) CategoryBits.ENEMY | CategoryBits.OBJECT;

        createBodyFixture(bodyCategoryBits, bodyMaskBits);
        createMeleeWeaponFixture(weaponMaskBits);
    }

    @Override
    public void handleInput(float delta) {
        if (setToKill) {
            return;
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            Constants.DEBUG = (Constants.DEBUG == true) ? false : true;
        }

        
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            swingWeapon();
        }
        
        // When player is attacking, movement is disabled.
        if (!isAttacking()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
                jump();
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                b2body.getPosition().y += 5;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                // if (!isCrouching) crouch();
                // else getUp();
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveLeft();
            }
        }
    }

    @Override
    public void inflictDamage(Character c, int damage) {
        super.inflictDamage(c, damage);
        Rumble.rumble(8 / Constants.PPM, .1f);
    }
    
    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        
        // Sets the player to be untouchable for a while.
        if (!isUntouchable) {
            Rumble.rumble(8 / Constants.PPM, .1f);
            isUntouchable = true;
            
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    if (!setToKill) {
                        isUntouchable = false;
                    }
                }
            }, 3f);
        }
    }

}