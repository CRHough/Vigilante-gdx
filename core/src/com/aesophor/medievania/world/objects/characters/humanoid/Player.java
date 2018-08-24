package com.aesophor.medievania.world.objects.characters.humanoid;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.screens.GameScreen;
import com.aesophor.medievania.utils.Utils;
import com.aesophor.medievania.world.objects.characters.Character;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player extends Character implements Humanoid {
    
    private int weaponDamage = 25; // temporary
    
    public Player(GameScreen screen, float x, float y) {
        super(screen.getAtlas().findRegion("bandit"), x, y);
        
        this.currentWorld = screen.getWorld();
        health = 100;
        
        // Create animations by extracting frames from the spritesheet.
        runAnimation = Utils.createAnimation(getTexture(), 8f / Medievania.PPM,      0, 7,  0, 3 * 80,  80, 80);
        jumpAnimation = Utils.createAnimation(getTexture(), 5f / Medievania.PPM,     0, 5,  0, 1 * 80,  80, 80);
        attackAnimation = Utils.createAnimation(getTexture(), 12f / Medievania.PPM,  1, 6,  0, 2 * 80,  80, 80);
        killedAnimation = Utils.createAnimation(getTexture(), 12f / Medievania.PPM,  0, 5,  0,      0,  80, 80);
        idleAnimation = new TextureRegion(getTexture(), 7 * 80, 2 * 80, 80, 80);
        
        // Sounds.
        footstepSound = Medievania.manager.get("Sound/FX/Player/footstep.mp3");
        hurtSound = Medievania.manager.get("Sound/FX/Player/hurt.wav");
        deathSound = Medievania.manager.get("Sound/FX/Player/death.mp3");
        weaponSwingSound = Medievania.manager.get("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        weaponHitSound = Medievania.manager.get("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        jumpSound = Medievania.manager.get("Sound/FX/Player/jump.wav", Sound.class);
        
        defineCharacter();
        
        
        setBounds(0, 0, 120 / Medievania.PPM, 120 / Medievania.PPM);
        setRegion(idleAnimation);
    }
    
    @Override
    public void update(float dt) {
        if (setToKill && !killed) {
            setRegion(killedAnimation.getKeyFrame(stateTimer, false));
            
            // Only destroy the body when animation has finished playing.
            if (killedAnimation.isAnimationFinished(stateTimer)) {
                System.out.println("player died.");
                deathSound.play();
                
                currentWorld.destroyBody(b2body);
                killed = true;
                
                stateTimer = 0;
            }
            
            stateTimer += dt; // clean up this pile of shit...
            
        } else if (!killed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 10 / Medievania.PPM - getHeight() / 2);
            setRegion(getFrame(dt));
        }
    }
    
    
    
    @Override
    public void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        
        b2body = currentWorld.createBody(bdef);
        
        FixtureDef fdef = new FixtureDef();
        PolygonShape body = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 20).scl(1 / Medievania.PPM);
        vertices[1] = new Vector2(5, 20).scl(1 / Medievania.PPM);
        vertices[2] = new Vector2(-5, -14).scl(1 / Medievania.PPM);
        vertices[3] = new Vector2(5, -14).scl(1 / Medievania.PPM);
        body.set(vertices);
        
        //CircleShape shape = new CircleShape();
        //shape.setRadius(15 / Medieval.PPM);
        
        fdef.filter.categoryBits = Medievania.PLAYER_BIT;
        fdef.filter.maskBits = Medievania.GROUND_BIT | Medievania.COIN_BIT | Medievania.BRICK_BIT | Medievania.ENEMY_BIT; // What player can collide with.
        
        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);
        
        
        /*
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Medieval.PPM, 13 / Medieval.PPM), new Vector2(-2 / Medieval.PPM, 13 / Medieval.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        
        b2body.createFixture(fdef).setUserData("head");
        */
        
        
        CircleShape weapon = new CircleShape();
        weapon.setRadius(28 / Medievania.PPM);
        
        fdef.filter.categoryBits = Medievania.MELEE_WEAPON_BIT;
        fdef.filter.maskBits = Medievania.ENEMY_BIT | Medievania.OBJECT_BIT; // What player can collide with.
        
        fdef.shape = weapon;
        fdef.isSensor = true; // a sensor won't collide with the world. 
        
        b2body.createFixture(fdef).setUserData(this);
    }
    
    
    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        hurtSound.play();
    }
    
    public int getHealth() {
        return health;
    }
    
    public boolean killed() {
        return killed;
    }
    
    public boolean isJumping() {
        return b2body.getLinearVelocity().y != 0;
    }
    
    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
        jumpSound.play();
    }
    
    
    
    public void setIsAttacking(boolean isAttacking) {
        weaponSwingSound.play();
        this.isAttacking = isAttacking;
    }


    public boolean hasTargetEnemy() {
        return !(targetEnemy == null);
    }
    
    public Character getTargetEnemy() {
        return targetEnemy;
    }
    
    public void setTargetEnemy(Character enemy) {
        targetEnemy = enemy;
    }
    
    public void attack(Character c) {
        Gdx.app.log("Player", String.format("deals %d damage to %s", weaponDamage, "knight"));
        c.receiveDamage(weaponDamage);
        
        weaponHitSound.play();
        
        float force = (facingRight) ? .6f : -.6f;
        //c.b2body.applyLinearImpulse(new Vector2(force, 0), enemy.b2body.getWorldCenter(), true);
    }
    
    public boolean facingRight() {
        return facingRight;
    }


    

}
