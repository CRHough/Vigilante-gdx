package com.aesophor.medievania.system;

import com.aesophor.medievania.component.AnimationComponent;
import com.aesophor.medievania.component.B2BodyComponent;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.SpriteComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

public class StaticSpriteRendererSystem extends IteratingSystem {

    private Batch batch;
    private Camera camera;
    private World world;

    public StaticSpriteRendererSystem(Batch batch, Camera camera, World world) {
        super(Family.all(SpriteComponent.class).exclude(AnimationComponent.class).get());

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
    protected void processEntity(Entity entity, float deltaTime) {
        SpriteComponent sprite = Mappers.SPRITE.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);

        float textureX = b2body.getBody().getPosition().x - sprite.getWidth() / 2;
        float textureY = b2body.getBody().getPosition().y - sprite.getHeight() / 2;
        sprite.setPosition(textureX, textureY);
        sprite.draw(batch);
    }

}