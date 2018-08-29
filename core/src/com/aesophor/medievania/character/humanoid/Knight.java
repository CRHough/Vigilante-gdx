package com.aesophor.medievania.character.humanoid;

import com.aesophor.medievania.character.Enemy;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.aesophor.medievania.util.box2d.BodyBuilder;
import com.aesophor.medievania.util.CategoryBits;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Knight extends Enemy implements Humanoid {
    
    private static final String TEXTURE_FILE = "Character/Knight/Knight.png";
    
    public Knight(AssetManager assets, World world, float x, float y) {
        super(assets.get(TEXTURE_FILE), world, x, y);
        
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
        Vector2[] bodyFixtureVertices = new Vector2[4];
        bodyFixtureVertices[0] = new Vector2(-5, 20);
        bodyFixtureVertices[1] = new Vector2(5, 20);
        bodyFixtureVertices[2] = new Vector2(-5, -14);
        bodyFixtureVertices[3] = new Vector2(5, -14);

        Vector2 meleeAttackFixtureVertices = new Vector2(attackRange, 0);


        BodyBuilder bodyBuilder = new BodyBuilder(currentWorld);

        b2body = bodyBuilder.type(BodyDef.BodyType.DynamicBody)
                .position(getX(), getY(), Constants.PPM)
                .buildBody();

        bodyFixture = bodyBuilder.newPolygonFixture(bodyFixtureVertices, Constants.PPM)
                .categoryBits(CategoryBits.ENEMY)
                .maskBits(CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL | CategoryBits.PLAYER | CategoryBits.MELEE_WEAPON | CategoryBits.CLIFF_MARKER)
                .setUserData(this)
                .buildFixture();

        meleeWeaponFixture = bodyBuilder.newCircleFixture(meleeAttackFixtureVertices, attackRange, Constants.PPM)
                .categoryBits(CategoryBits.MELEE_WEAPON)
                .maskBits(CategoryBits.PLAYER | CategoryBits.OBJECT)
                .isSensor(true)
                .setUserData(this)
                .buildFixture();
    }

}