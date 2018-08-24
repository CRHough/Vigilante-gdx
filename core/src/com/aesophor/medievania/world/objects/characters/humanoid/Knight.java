package com.aesophor.medievania.world.objects.characters.humanoid;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.screens.GameScreen;
import com.aesophor.medievania.world.objects.characters.Enemy;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Knight extends Enemy implements Humanoid {
    
    private TextureRegion knightStand;
    private Animation<TextureRegion> knightWalk;
    private Animation<TextureRegion> knightKilled;
    
    private float stateTimer;
    
    
    public Knight(GameScreen screen, float x, float y) {
        super(screen, x, y);
        
        
        TextureAtlas atlas = Medievania.manager.get("Character/Knight/Knight.pack");
        AtlasRegion atlasRegion = atlas.findRegion("knight");
        
        Array<TextureRegion> frames = new Array<>();
        
        // Knight idle animation.
        for (int i = 0; i <= 7; i++) {
            frames.add(new TextureRegion(atlasRegion, i * 42, 0 * 42, 42, 42));
        }
        knightWalk = new Animation<>(12f / Medievania.PPM, frames);
        frames.clear();
        
        // Knight walk animation.
        for (int i = 0; i <= 7; i++) {
            frames.add(new TextureRegion(atlasRegion, i * 42, 0 * 42, 42, 42));
        }
        knightWalk = new Animation<>(12f / Medievania.PPM, frames);
        frames.clear();
        
        // Knight killed animation.
        for (int i = 12; i <= 20; i++) {
            frames.add(new TextureRegion(atlasRegion, i * 42, 0 * 42, 42, 42));
        }
        knightKilled = new Animation<>(12f / Medievania.PPM, frames);
        frames.clear();
        
        
        // Knight stand animation.
        knightStand = new TextureRegion(atlasRegion, 8 * 42, 0 * 42, 42, 42);
        
        setBounds(0, 0, 50 / Medievania.PPM, 50 / Medievania.PPM);
        setRegion(knightStand);
        
    }
    
    @Override
    public void update(float dt) {
        stateTimer += dt;
        
        if (setToKill && !killed) {
            setRegion(knightKilled.getKeyFrame(stateTimer, false));
            
            // Only destroy the body when animation has finished playing.
            if (knightKilled.isAnimationFinished(stateTimer)) {
                System.out.println("You have killed an enemy.");
                
                deathSound.play();
                
                killed = true;
                
                stateTimer = 0;
                world.destroyBody(b2body);
            }
            
        } else if (!killed) {
            //b2body.setLinearVelocity(0.3f, 0);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 5 / Medievania.PPM - getHeight() / 2);
            //setRegion(knightWalk.getKeyFrame(stateTimer, true));
        }
    }

    @Override
    protected void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        
        b2body = world.createBody(bdef);
        
        FixtureDef fdef = new FixtureDef();
        PolygonShape body = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-8, 25).scl(1 / Medievania.PPM);
        vertices[1] = new Vector2(8, 25).scl(1 / Medievania.PPM);
        vertices[2] = new Vector2(-8, -15).scl(1 / Medievania.PPM);
        vertices[3] = new Vector2(7, -15).scl(1 / Medievania.PPM);
        body.set(vertices);
        
        //CircleShape shape = new CircleShape();
        //shape.setRadius(12 / Medieval.PPM);
        
        fdef.filter.categoryBits = Medievania.ENEMY_BIT;
        fdef.filter.maskBits = Medievania.GROUND_BIT | Medievania.PLAYER_BIT | Medievania.MELEE_WEAPON_BIT; // What it can collide with.
        
        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);;
    }


    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        hurtSound.play();
    }

}
