package com.aesophor.medieval.sprites;

import com.aesophor.medieval.Medieval;
import com.aesophor.medieval.game.characters.Character;
import com.aesophor.medieval.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite {
    
    public World world;
    public Body b2body;
    
    private Character.State currentState;
    private Character.State previousState;
    
    private TextureRegion playerStand;
    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerJump;
    private Animation<TextureRegion> playerAttack;
    private Animation<TextureRegion> playerKilled;
    
    private int health;
    
    private float stateTimer;
    private boolean facingRight;
    private boolean isJumping;
    private boolean isAttacking;
    
    private Enemy targetEnemy;
    private int weaponDamage = 25;
    
    private boolean setToKill;
    private boolean killed;
    
    private Music footstepSound;
    private Sound hurtSound;
    private Sound deathSound;
    
    public Player(World world, GameScreen screen) {
        super(screen.getAtlas().findRegion("bandit"));
        this.world = world;
        
        currentState = Character.State.STANDING;
        previousState = Character.State.STANDING;
        
        health = 100;
        
        stateTimer = 0;
        facingRight = true;
        isJumping = false;
        
        // Run animation.
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i <= 7; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 3 * 80, 80, 80));
        }
        
        playerRun = new Animation<TextureRegion>(8f / Medieval.PPM, frames); // duration of each frame
        frames.clear();
        
        // Jump animation.
        for (int i = 0; i <= 5; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 1 * 80, 80, 80));
        }
        playerJump = new Animation<TextureRegion>(5f / Medieval.PPM, frames);
        frames.clear();
        
        // Attack animation.
        for (int i = 1; i <= 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 2 * 80, 80, 80));
        }
        playerAttack = new Animation<TextureRegion>(12f / Medieval.PPM, frames);
        frames.clear();
        
        // Killed animation
        for (int i = 0; i <= 5; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 0 * 80, 80, 80));
        }
        playerKilled = new Animation<TextureRegion>(12f / Medieval.PPM, frames);
        frames.clear();
        
        
        // Sounds.
        footstepSound = Medieval.manager.get("Sound/FX/Player/footstep.mp3");
        hurtSound = Medieval.manager.get("Sound/FX/Player/hurt.wav");
        deathSound = Medieval.manager.get("Sound/FX/Player/death.mp3");
        
        playerStand = new TextureRegion(getTexture(), 7 * 80, 2 * 80, 80, 80);
        
        definePlayer();
        
        
        setBounds(0, 0, 80 / Medieval.PPM, 80 / Medieval.PPM);
        setRegion(playerStand);
    }
    
    
    public void update(float dt) {
        if (setToKill && !killed) {
            setRegion(playerKilled.getKeyFrame(stateTimer, false));
            
            // Only destroy the body when animation has finished playing.
            if (playerKilled.isAnimationFinished(stateTimer)) {
                System.out.println("player died.");
                deathSound.play();
                
                world.destroyBody(b2body);
                killed = true;
                
                stateTimer = 0;
            }
            
            stateTimer += dt; // clean up this pile of shit...
            
        } else if (!killed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 5 / Medieval.PPM - getHeight() / 2);
            setRegion(getFrame(dt));
        }
    }
    
    public TextureRegion getFrame(float dt) {
        currentState = getState();
        
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer, false);
                break;
            case RUNNING:
                //footstepSound.setLooping(true);
                //footstepSound.play();
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = (TextureRegion) playerAttack.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
            case STANDING:
            default:
                //footstepSound.stop();
                region = playerStand;
                break;
        }
        
        if ((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false); // flip x, flip y.
            facingRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }
        
        if (isAttacking() && stateTimer >= 1) {
            isAttacking = false;
        }
        
        stateTimer = (currentState == previousState) ? stateTimer + dt : 0;
        previousState = currentState;
        
        return region;
    }
    
    public Character.State getState() {
        if (isAttacking()) {
            return Character.State.ATTACKING;
        } else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == Character.State.JUMPING)) {
            return Character.State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return Character.State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return Character.State.RUNNING;
        } else {
            return Character.State.STANDING;
        }
    }
    
    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Medieval.PPM, 150 / Medieval.PPM); //temporary (player spawn coordinate)
        bdef.type = BodyDef.BodyType.DynamicBody;
        
        b2body = world.createBody(bdef);
        
        FixtureDef fdef = new FixtureDef();
        PolygonShape body = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 13).scl(1 / Medieval.PPM);
        vertices[1] = new Vector2(5, 13).scl(1 / Medieval.PPM);
        vertices[2] = new Vector2(-5, -10).scl(1 / Medieval.PPM);
        vertices[3] = new Vector2(5, -10).scl(1 / Medieval.PPM);
        body.set(vertices);
        
        //CircleShape shape = new CircleShape();
        //shape.setRadius(15 / Medieval.PPM);
        
        fdef.filter.categoryBits = Medieval.PLAYER_BIT;
        fdef.filter.maskBits = Medieval.GROUND_BIT | Medieval.COIN_BIT | Medieval.BRICK_BIT | Medieval.ENEMY_BIT; // What player can collide with.
        
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
        weapon.setRadius(15 / Medieval.PPM);
        
        fdef.filter.categoryBits = Medieval.MELEE_WEAPON_BIT;
        fdef.filter.maskBits = Medieval.ENEMY_BIT | Medieval.OBJECT_BIT; // What player can collide with.
        
        fdef.shape = weapon;
        fdef.isSensor = true; // a sensor won't collide with the world. 
        
        b2body.createFixture(fdef).setUserData(this);
    }
    
    
    public void inflictDamage(int damage) {
        health -= damage;
        
        if (health <= 0) {
            setToKill = true;
        }
        
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
    }
    
    public boolean isAttacking() {
        return isAttacking;
    }
    
    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }


    public boolean hasTargetEnemy() {
        return !(targetEnemy == null);
    }
    
    public Enemy getTargetEnemy() {
        return targetEnemy;
    }
    
    public void setTargetEnemy(Enemy enemy) {
        targetEnemy = enemy;
    }
    
    public void attack(Enemy enemy) {
        Gdx.app.log("Player", String.format("deals %d damage to %s", weaponDamage, "knight"));
        enemy.inflictDamage(weaponDamage);
    }

}
