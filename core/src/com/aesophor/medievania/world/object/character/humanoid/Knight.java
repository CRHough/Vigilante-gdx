package com.aesophor.medievania.world.object.character.humanoid;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.constant.Constants;
import com.aesophor.medievania.screen.GameScreen;
import com.aesophor.medievania.util.Utils;
import com.aesophor.medievania.world.object.character.Enemy;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Knight extends Enemy implements Humanoid {
    
    private static final String TEXTURE_FILE = "Character/Knight/Knight.png";
    
    public Knight(GameScreen screen, float x, float y) {
        super(Medievania.manager.get(TEXTURE_FILE), screen.getWorld(), x, y);
        
        health = 100;
        
        // Knight stand animation.
        idleAnimation = new TextureRegion(getTexture(), 8 * 42, 0 * 42, 42, 42);
        runAnimation = Utils.createAnimation(getTexture(), 12f / Constants.PPM, 0, 7, 0, 0, 42, 42);
        killedAnimation = Utils.createAnimation(getTexture(), 24f / Constants.PPM, 12, 19, 0, 0, 42, 42);
        
        // Sounds.
        hurtSound = Medievania.manager.get("Sound/FX/Player/hurt.wav");
        deathSound = Medievania.manager.get("Sound/FX/Player/death.mp3");
        
        defineCharacter();
        
        setBounds(0, 0, 50 / Constants.PPM, 50 / Constants.PPM);
        setRegion(idleAnimation);
        
    }
    
    @Override
    public void update(float dt) {
        stateTimer += dt;
        
        if (setToKill && !killed) {
            setRegion(killedAnimation.getKeyFrame(stateTimer, false));
            
            // Only destroy the body when animation has finished playing.
            if (killedAnimation.isAnimationFinished(stateTimer)) {
                System.out.println("You have killed an enemy.");
                
                deathSound.play();
                
                killed = true;
                
                stateTimer = 0;
                currentWorld.destroyBody(b2body);
            }
            
        } else if (!killed) {
            //b2body.setLinearVelocity(0.3f, 0);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 5 / Constants.PPM - getHeight() / 2);
            //setRegion(knightWalk.getKeyFrame(stateTimer, true));
        }
    }

    @Override
    protected void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        
        b2body = currentWorld.createBody(bdef);
        
        FixtureDef fdef = new FixtureDef();
        PolygonShape body = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-8, 25).scl(1 / Constants.PPM);
        vertices[1] = new Vector2(8, 25).scl(1 / Constants.PPM);
        vertices[2] = new Vector2(-8, -15).scl(1 / Constants.PPM);
        vertices[3] = new Vector2(7, -15).scl(1 / Constants.PPM);
        body.set(vertices);
        
        
        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT | Constants.PLAYER_BIT | Constants.MELEE_WEAPON_BIT; // What it can collide with.
        
        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);;
    }



}
