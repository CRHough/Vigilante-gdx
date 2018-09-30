package com.aesophor.medievania.system.graphics;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.StateComponent;
import com.aesophor.medievania.component.graphics.AnimationComponent;
import com.aesophor.medievania.component.graphics.IconComponent;
import com.aesophor.medievania.component.graphics.SpriteComponent;
import com.aesophor.medievania.component.physics.B2BodyComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

public class StaticSpriteRendererSystem extends IteratingSystem {

    private final Batch batch;
    private final Camera camera;
    private final World world;

    public StaticSpriteRendererSystem(Batch batch, Camera camera, World world) {
        super(Family.all(SpriteComponent.class).exclude(StateComponent.class, AnimationComponent.class).get());

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
        IconComponent icon = Mappers.ICON.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);

        float textureX = b2body.getBody().getPosition().x - icon.getWidth() / 2;
        float textureY = b2body.getBody().getPosition().y - icon.getHeight() / 2;
        icon.setPosition(textureX, textureY);
        icon.draw(batch);
    }

}