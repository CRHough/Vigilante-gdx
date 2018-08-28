package com.aesophor.medievania.world.character;

import com.aesophor.medievania.constants.CategoryBits;
import com.aesophor.medievania.constants.Constants;
import com.aesophor.medievania.screen.MainGameScreen;
import com.aesophor.medievania.util.Utils;
import com.aesophor.medievania.world.character.humanoid.Humanoid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Character implements Controllable, Humanoid {
    
    private static final String TEXTURE_FILE = "Character/Bandit/Bandit.png";
    
    private AssetManager assets;
    
    public Player(MainGameScreen screen, float x, float y) {
        super(screen.getGSM().getAssets().get(TEXTURE_FILE), screen.getWorld(), x, y);
        
        assets = screen.getGSM().getAssets();
        
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
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = currentWorld.createBody(bdef);
        
        // Create body fixture.
        // Fixture position in box2d is in relation to the body position.
        FixtureDef fdef = new FixtureDef();
        PolygonShape body = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 20).scl(1 / Constants.PPM);
        vertices[1] = new Vector2(5, 20).scl(1 / Constants.PPM);
        vertices[2] = new Vector2(-5, -14).scl(1 / Constants.PPM);
        vertices[3] = new Vector2(5, -14).scl(1 / Constants.PPM);
        body.set(vertices);
        
        fdef.shape = body;
        fdef.filter.categoryBits = CategoryBits.PLAYER;
        fdef.filter.maskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.ENEMY | CategoryBits.MELEE_WEAPON; // What player can collide with.
        bodyFixture = b2body.createFixture(fdef);
        bodyFixture.setUserData(this);
        body.dispose();
        
        
        // Create weapon fixture.
        CircleShape weapon = new CircleShape();
        weapon.setPosition(new Vector2(attackRange / Constants.PPM, 0));
        weapon.setRadius(attackRange / Constants.PPM);
        
        fdef.shape = weapon;
        fdef.isSensor = true; // a sensor won't collide with the world.
        fdef.filter.categoryBits = CategoryBits.MELEE_WEAPON;
        fdef.filter.maskBits = CategoryBits.ENEMY | CategoryBits.OBJECT; // What player can collide with.
        
        meleeAttackFixture = b2body.createFixture(fdef);
        meleeAttackFixture.setUserData(this);
        weapon.dispose();
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
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        
        
        // Sets the player to be untouchable for a while.
        if (!isUntouchable) {
            isUntouchable = true;
            
            Timer.schedule(new Task(){
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