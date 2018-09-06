package com.aesophor.medievania.character;

import com.aesophor.medievania.GameWorldManager;
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

public class Player extends Character implements Humanoid, Controllable {
    
    private static final String TEXTURE_FILE = "character/bandit/Bandit.png";

    private GameWorldManager gameWorldManager;
    private Portal currentPortal;
    
    public Player(GameWorldManager gameWorldManager, float x, float y) {
        super(gameWorldManager.getAssets().get(TEXTURE_FILE), gameWorldManager.getWorld(), x, y);
        this.gameWorldManager = gameWorldManager;

        cc.bodyWidth = 10;
        cc.bodyHeight = 34;

        cc.health = 100;
        cc.movementSpeed = .3f;
        cc.jumpHeight = 3f;
        cc.attackForce = 1f;
        cc.attackTime = 1f;
        cc.attackRange = 15;
        cc.attackDamage = 25;
        
        // Create animations by extracting frames from the spritesheet.
        // CLEAN IT UP LATER !!!
        Animation<TextureRegion> idleAnimation = Utils.createAnimation(spc.sprite.getTexture(), 10f / Constants.PPM, 0, 0, 7 * 80, 2 * 80, 80, 80);
        Animation<TextureRegion> runAnimation = Utils.createAnimation(spc.sprite.getTexture(), 9f / Constants.PPM, 0, 7,  0, 3 * 80,  80, 80);
        Animation<TextureRegion> jumpAnimation = Utils.createAnimation(spc.sprite.getTexture(), 10f / Constants.PPM, 0, 3,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> fallAnimation = Utils.createAnimation(spc.sprite.getTexture(), 10f / Constants.PPM, 4, 4,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> crouchAnimation = Utils.createAnimation(spc.sprite.getTexture(), 10f / Constants.PPM, 5, 5,  0, 1 * 80,  80, 80);
        Animation<TextureRegion> attackAnimation = Utils.createAnimation(spc.sprite.getTexture(), 12f / Constants.PPM, 1, 6,  0, 2 * 80,  80, 80);
        Animation<TextureRegion> killedAnimation = Utils.createAnimation(spc.sprite.getTexture(), 24f / Constants.PPM, 0, 5,  0,      0,  80, 80);

        ac.animations.put(State.IDLE, idleAnimation);
        ac.animations.put(State.RUNNING, runAnimation);
        ac.animations.put(State.JUMPING, jumpAnimation);
        ac.animations.put(State.FALLING, fallAnimation);
        ac.animations.put(State.CROUCHING, crouchAnimation);
        ac.animations.put(State.ATTACKING, attackAnimation);
        ac.animations.put(State.KILLED, killedAnimation);

        
        // Sounds.
        footstepSound = gameWorldManager.getAssets().get("sfx/player/footstep.mp3");
        hurtSound = gameWorldManager.getAssets().get("sfx/player/hurt.wav");
        deathSound = gameWorldManager.getAssets().get("sfx/player/death.mp3");
        weaponSwingSound = gameWorldManager.getAssets().get("sfx/player/weapon_swing.ogg", Sound.class);
        weaponHitSound = gameWorldManager.getAssets().get("sfx/player/weapon_hit.ogg", Sound.class);
        jumpSound = gameWorldManager.getAssets().get("sfx/player/jump.wav", Sound.class);

        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.PLAYER;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PORTAL | CategoryBits.ENEMY | CategoryBits.MELEE_WEAPON;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        short weaponMaskBits = CategoryBits.ENEMY | CategoryBits.OBJECT;
        super.defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);

        spc.sprite.setBounds(0, 0, 120 / Constants.PPM, 120 / Constants.PPM);
    }

    @Override
    public void handleInput(float delta) {

    }

    public Portal getCurrentPortal() {
        return currentPortal;
    }

    public void setCurrentPortal(Portal currentPortal) {
        this.currentPortal = currentPortal;
    }

    public void reposition(Vector2 position) {
        bc.body.setTransform(position, 0);
    }

    public void reposition(float x, float y) {
        bc.body.setTransform(x, y, 0);
    }

    @Override
    public void inflictDamage(Character c, int damage) {
        if ((stc.facingRight && c.facingRight()) || (!stc.facingRight && !c.facingRight())) {
            damage *= 2;
            gameWorldManager.getNotificationArea().show("Critical hit!");
        }

        super.inflictDamage(c, damage);

        gameWorldManager.getDamageIndicator().show(c, damage);
        gameWorldManager.getNotificationArea().show(String.format("You dealt %d pts damage to %s", damage, c.getName()));
        CameraShake.shake(8 / Constants.PPM, .1f);

        if (c.isSetToKill()) {
            gameWorldManager.getNotificationArea().show(String.format("You earned 10 exp."));
        }
    }
    
    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);

        // Sets the player to be untouchable for a while.
        if (!stc.invincible) {
            CameraShake.shake(8 / Constants.PPM, .1f);
            stc.invincible = true;

            Timer.schedule(new Task() {
                @Override
                public void run() {
                    if (!stc.setToKill) {
                        stc.invincible = false;
                    }
                }
            }, 3f);
        }
    }

}