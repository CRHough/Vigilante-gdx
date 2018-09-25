package com.aesophor.medievania.system;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.graphics.AnimationComponent;
import com.aesophor.medievania.component.graphics.SpriteComponent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class AnimatedSpriteRendererSystem extends IteratingSystem {

    private final PooledEngine engine;
    private final Batch batch;
    private final Camera camera;
    private final World world;

    public AnimatedSpriteRendererSystem(PooledEngine engine, Batch batch, Camera camera, World world) {
        super(Family.all(AnimationComponent.class).get());

        this.engine = engine;
        this.batch = batch;
        this.camera = camera;
        this.world = world;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.update(delta);
        batch.end();
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        AnimationComponent<TextureRegion> animation = Mappers.ANIMATION.get(entity); // look into this later.
        SpriteComponent sprite = Mappers.SPRITE.get(entity);

        sprite.setRegion(animation.getKeyFrame(animation.getTimer(), false));
        animation.update(delta);
        sprite.draw(batch);

        if (animation.isAnimationFinished(animation.getTimer())) {
            engine.removeEntity(entity);
        }
    }

}