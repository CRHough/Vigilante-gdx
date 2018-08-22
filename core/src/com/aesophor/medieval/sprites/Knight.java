package com.aesophor.medieval.sprites;

import com.aesophor.medieval.Medieval;
import com.aesophor.medieval.screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Knight extends Enemy {
    
    private TextureRegion knightStand;
    private Animation<TextureRegion> knightWalk;
    
    private float stateTimer;
    private boolean setToKill;
    private boolean killed;
    
    public Knight(World world, GameScreen screen, float x, float y) {
        super(world, screen, x, y);
        
        
        TextureAtlas atlas = Medieval.manager.get("Character/Knight/Knight.pack");
        AtlasRegion atlasRegion = atlas.findRegion("knight");
        Array<TextureRegion> frames = new Array<>();
        
        // Knight idle animation.
        for (int i = 0; i <= 7; i++) {
            frames.add(new TextureRegion(atlasRegion, i * 42, 0 * 42, 42, 42));
        }
        knightWalk = new Animation<>(12f / Medieval.PPM, frames);
        frames.clear();
        
        // Knight walk animation.
        for (int i = 0; i <= 7; i++) {
            frames.add(new TextureRegion(atlasRegion, i * 42, 0 * 42, 42, 42));
        }
        knightWalk = new Animation<>(12f / Medieval.PPM, frames);
        frames.clear();
        
        // Knight stand animation.
        knightStand = new TextureRegion(atlasRegion, 8 * 42, 0 * 42, 42, 42);
        
        setBounds(0, 0, 42 / Medieval.PPM, 42 / Medieval.PPM);
        setRegion(knightStand);
        
    }
    
    
    public void update(float dt) {
        stateTimer += dt;
        
        if (setToKill && !killed) {
            world.destroyBody(b2body);
            killed = true;
            //setRegion(knightKilled);
            
        } else if (!killed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 5 / Medieval.PPM - getHeight() / 2);
            //setRegion(knightWalk.getKeyFrame(stateTimer, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); //temporary spawn point
        bdef.type = BodyDef.BodyType.DynamicBody;
        
        b2body = world.createBody(bdef);
        
        FixtureDef fdef = new FixtureDef();
        PolygonShape body = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 19).scl(1 / Medieval.PPM);
        vertices[1] = new Vector2(5, 19).scl(1 / Medieval.PPM);
        vertices[2] = new Vector2(-5, -11).scl(1 / Medieval.PPM);
        vertices[3] = new Vector2(5, -11).scl(1 / Medieval.PPM);
        body.set(vertices);
        
        //CircleShape shape = new CircleShape();
        //shape.setRadius(12 / Medieval.PPM);
        
        fdef.filter.categoryBits = Medieval.ENEMY_BIT;
        fdef.filter.maskBits = Medieval.GROUND_BIT | Medieval.PLAYER_BIT | Medieval.COIN_BIT | Medieval.BRICK_BIT; // What player can collide with.
        
        fdef.shape = body;
        b2body.createFixture(fdef).setUserData("body");;
    }

}
