package com.aesophor.vigilante.system.graphics;

import com.aesophor.vigilante.component.Mappers;
import com.aesophor.vigilante.component.graphics.AnimationComponent;
import com.aesophor.vigilante.component.graphics.SpriteComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSpriteRendererSystem extends IteratingSystem {

    private final PooledEngine engine;
    private final AssetManager assets;
    private final Batch batch;
    private final Camera camera;

    public AnimatedSpriteRendererSystem(PooledEngine engine, AssetManager assets, Batch batch, Camera camera) {
        super(Family.all(AnimationComponent.class).get());

        this.engine = engine;
        this.assets = assets;
        this.batch = batch;
        this.camera = camera;
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
            assets.unload(assets.getAssetFileName(Mappers.SPRITE.get(entity).getTexture()));
        }
    }

}