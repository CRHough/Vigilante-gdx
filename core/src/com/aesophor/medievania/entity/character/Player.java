package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.GameWorldManager;
import com.aesophor.medievania.component.ControllableComponent;
import com.aesophor.medievania.component.SoundType;
import com.aesophor.medievania.component.State;
import com.aesophor.medievania.map.Portal;
import com.aesophor.medievania.util.CameraShake;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Character implements Humanoid {

    private static final String TEXTURE_FILE = "character/bandit/Bandit.png";

    private GameWorldManager gameWorldManager;
    private Portal currentPortal;

    public Player(GameWorldManager gameWorldManager, float x, float y) {
        super(gameWorldManager.getAssets().get(TEXTURE_FILE), gameWorldManager.getWorld(), x, y);
        this.gameWorldManager = gameWorldManager;

        add(new ControllableComponent());

        stats.name = "Michael";
        stats.bodyWidth = 10;
        stats.bodyHeight = 34;

        stats.health = 100;
        stats.movementSpeed = .3f;
        stats.jumpHeight = 3f;
        stats.attackForce = 1f;
        stats.attackTime = 1.8f;
        stats.attackRange = 15;
        stats.attackDamage = 25;

        // Create animations by extracting frames from the spritesheet.
        // CLEAN IT UP LATER !!!
        Animation<TextureRegion> idleAnimation = Utils.createAnimation(sprite.sprite.getTexture(), 10f / Constants.PPM, 0, 0, 7 * 80, 2 * 80, 80, 80);
        Animation<TextureRegion> runAnimation = Utils.createAnimation(sprite.sprite.getTexture(), 20f / Constants.PPM, 0, 7,  0, 3 * 80,  80, 80);
        Animation<TextureRegion> jumpAnimation = Utils.createAnimation(sprite.sprite.getTexture(), 10f / Constants.PPM, 0, 3,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> fallAnimation = Utils.createAnimation(sprite.sprite.getTexture(), 10f / Constants.PPM, 4, 4,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> crouchAnimation = Utils.createAnimation(sprite.sprite.getTexture(), 10f / Constants.PPM, 5, 5,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> attackAnimation = Utils.createAnimation(sprite.sprite.getTexture(), 20f / Constants.PPM, 1, 6,  0, 2 * 80,  80, 80);
        Animation<TextureRegion> killedAnimation = Utils.createAnimation(sprite.sprite.getTexture(), 30f / Constants.PPM, 0, 5,  0,      0,  80, 80);

        animations.put(State.IDLE, idleAnimation);
        animations.put(State.RUNNING, runAnimation);
        animations.put(State.JUMPING, jumpAnimation);
        animations.put(State.FALLING, fallAnimation);
        animations.put(State.CROUCHING, crouchAnimation);
        animations.put(State.ATTACKING, attackAnimation);
        animations.put(State.KILLED, killedAnimation);


        // Sounds.
        //Sound footstepSound = gameWorldManager.getAssets().get("sfx/player/footstep.mp3");
        Sound hurtSound = gameWorldManager.getAssets().get("sfx/player/hurt.wav");
        Sound deathSound = gameWorldManager.getAssets().get("sfx/player/death.mp3");
        Sound weaponSwingSound = gameWorldManager.getAssets().get("sfx/player/weapon_swing.ogg", Sound.class);
        Sound weaponHitSound = gameWorldManager.getAssets().get("sfx/player/weapon_hit.ogg", Sound.class);
        Sound jumpSound = gameWorldManager.getAssets().get("sfx/player/jump.wav", Sound.class);

        //sounds.put(SoundType.FOOTSTEP, footstepSound);
        sounds.put(SoundType.JUMP, jumpSound);
        sounds.put(SoundType.HURT, hurtSound);
        sounds.put(SoundType.DEATH, deathSound);
        sounds.put(SoundType.WEAPON_SWING, weaponSwingSound);
        sounds.put(SoundType.WEAPON_HIT, weaponHitSound);


        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.PLAYER;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PORTAL | CategoryBits.ENEMY | CategoryBits.MELEE_WEAPON;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        short weaponMaskBits = CategoryBits.ENEMY | CategoryBits.OBJECT;
        super.defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);

        sprite.sprite.setBounds(0, 0, 120 / Constants.PPM, 120 / Constants.PPM);
    }

    public Portal getCurrentPortal() {
        return currentPortal;
    }

    public void setCurrentPortal(Portal currentPortal) {
        this.currentPortal = currentPortal;
    }

    public void reposition(Vector2 position) {
        b2body.body.setTransform(position, 0);
    }

    public void reposition(float x, float y) {
        b2body.body.setTransform(x, y, 0);
    }

    @Override
    public void inflictDamage(Character c, int damage) {
        if ((state.facingRight && c.state.facingRight()) || (!state.facingRight && !c.state.facingRight())) {
            damage *= 2;
            gameWorldManager.getNotificationArea().show("Critical hit!");
        }

        super.inflictDamage(c, damage);

        gameWorldManager.getDamageIndicator().show(c, damage);
        gameWorldManager.getNotificationArea().show(String.format("You dealt %d pts damage to %s", damage, c.getName()));
        CameraShake.shake(8 / Constants.PPM, .1f);

        if (c.state.isSetToKill()) {
            gameWorldManager.getNotificationArea().show(String.format("You earned 10 exp."));
        }
    }

    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);

        // Sets the player to be untouchable for a while.
        if (!state.invincible) {
            CameraShake.shake(8 / Constants.PPM, .1f);
            state.invincible = true;

            Timer.schedule(new Task() {
                @Override
                public void run() {
                    if (!state.setToKill) {
                        state.invincible = false;
                    }
                }
            }, 3f);
        }
    }

}