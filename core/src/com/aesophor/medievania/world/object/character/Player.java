package com.aesophor.medievania.world.object.character;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.constant.Constants;
import com.aesophor.medievania.screen.GameScreen;
import com.aesophor.medievania.util.Utils;
import com.aesophor.medievania.world.object.character.humanoid.Humanoid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player extends Character implements Controllable, Humanoid {
    
    private static final String TEXTURE_FILE = "Character/Bandit/Bandit.png";
    
    public Player(GameScreen screen, float x, float y) {
        super(Medievania.manager.get(TEXTURE_FILE), screen.getWorld(), x, y);
        
        this.currentWorld = screen.getWorld();
        health = 100;
        movementSpeed = .25f;
        jumpHeight = 3f;
        attackForce = .6f;
        attackTime = 1f;
        attackRange = 14;
        attackDamage = 25;
        
        // Create animations by extracting frames from the spritesheet.
        idleAnimation = new TextureRegion(getTexture(), 7 * 80, 2 * 80, 80, 80);
        runAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,      0, 7,  0, 3 * 80,  80, 80);
        jumpAnimation = Utils.createAnimation(getTexture(), 3f / Constants.PPM,     0, 3,  0, 1 * 80,  80, 80);
        fallAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,    4, 4,  0, 1 * 80,  80, 80);
        crouchAnimation = Utils.createAnimation(getTexture(), 10f / Constants.PPM,  5, 5,  0, 1 * 80,  80, 80);
        attackAnimation = Utils.createAnimation(getTexture(), 12f / Constants.PPM,  1, 6,  0, 2 * 80,  80, 80);
        killedAnimation = Utils.createAnimation(getTexture(), 24f / Constants.PPM,  0, 5,  0,      0,  80, 80);
        
        // Sounds.
        footstepSound = Medievania.manager.get("Sound/FX/Player/footstep.mp3");
        hurtSound = Medievania.manager.get("Sound/FX/Player/hurt.wav");
        deathSound = Medievania.manager.get("Sound/FX/Player/death.mp3");
        weaponSwingSound = Medievania.manager.get("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        weaponHitSound = Medievania.manager.get("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        jumpSound = Medievania.manager.get("Sound/FX/Player/jump.wav", Sound.class);
        
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
        
        FixtureDef fdef = new FixtureDef();
        PolygonShape body = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 20).scl(1 / Constants.PPM);
        vertices[1] = new Vector2(5, 20).scl(1 / Constants.PPM);
        vertices[2] = new Vector2(-5, -14).scl(1 / Constants.PPM);
        vertices[3] = new Vector2(5, -14).scl(1 / Constants.PPM);
        body.set(vertices);
        
        fdef.shape = body;
        fdef.filter.categoryBits = Constants.PLAYER_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT | Constants.ENEMY_BIT | Constants.MELEE_WEAPON_BIT; // What player can collide with.
        bodyFixture = b2body.createFixture(fdef);
        bodyFixture.setUserData(this);
        
        
        CircleShape weapon = new CircleShape();
        weapon.setPosition(new Vector2(((b2body.getLocalCenter().x + attackRange) / Constants.PPM + attackRange) / Constants.PPM, getY() / Constants.PPM));
        weapon.setRadius(attackRange / Constants.PPM);
        
        fdef.shape = weapon;
        fdef.isSensor = true; // a sensor won't collide with the world.
        fdef.filter.categoryBits = Constants.MELEE_WEAPON_BIT;
        fdef.filter.maskBits = Constants.ENEMY_BIT | Constants.OBJECT_BIT; // What player can collide with.
        
        meleeAttackFixture = b2body.createFixture(fdef);
        meleeAttackFixture.setUserData(this);
    }
    
    @Override
    public void handleInput(float delta) {
        if (setToKill) {
            return;
        }
        
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            swingWeapon();
        }
        
        // When player is attacking, movement is disabled.
        if (!isAttacking()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
                jump();
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

}