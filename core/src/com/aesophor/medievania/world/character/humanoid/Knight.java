package com.aesophor.medievania.world.character.humanoid;

import com.aesophor.medievania.constants.CategoryBits;
import com.aesophor.medievania.constants.Constants;
import com.aesophor.medievania.screen.MainGame;
import com.aesophor.medievania.util.Utils;
import com.aesophor.medievania.world.character.Enemy;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Knight extends Enemy implements Humanoid {
    
    private static final String TEXTURE_FILE = "Character/Knight/Knight.png";
    
    private AssetManager assets;
    
    public Knight(MainGame screen, float x, float y) {
        super(screen.getGSM().getAssets().get(TEXTURE_FILE), screen.getWorld(), x, y);
        
        assets = screen.getGSM().getAssets();
        
        health = 100;
        movementSpeed = .25f;
        jumpHeight = 3.3f;
        attackForce = .6f;
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
        
        defineBody();
        
        setBounds(0, 0, 50 / Constants.PPM, 50 / Constants.PPM);
        setRegion(idleAnimation);
        
        facingRight = false;
    }
    
    

    // Refactor this part into Enemy class.
    @Override
    protected void defineBody() {
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
        fdef.filter.categoryBits = CategoryBits.ENEMY;
        fdef.filter.maskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.CLIFF_MARKER | CategoryBits.PLAYER | CategoryBits.MELEE_WEAPON; // What it can collide with.
        bodyFixture = b2body.createFixture(fdef);
        bodyFixture.setUserData(this);
        body.dispose();
        
        
        CircleShape weapon = new CircleShape();
        weapon.setPosition(new Vector2((getX() + attackRange) / Constants.PPM, getY() / Constants.PPM));
        weapon.setRadius(attackRange / Constants.PPM);
        
        fdef.shape = weapon;
        fdef.isSensor = true; // a sensor won't collide with the world.
        fdef.filter.categoryBits = CategoryBits.MELEE_WEAPON;
        fdef.filter.maskBits = CategoryBits.PLAYER | CategoryBits.OBJECT;
        
        meleeAttackFixture = b2body.createFixture(fdef);
        meleeAttackFixture.setUserData(this);
        weapon.dispose();
    }

}