package com.aesophor.medievania.system;

import com.aesophor.medievania.component.character.CharacterAnimationComponent;
import com.aesophor.medievania.component.character.StateComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

public class EquipmentRendererSystem extends IteratingSystem {

    private final Batch batch;
    private final Camera camera;
    private final World world;

    public EquipmentRendererSystem(Batch batch, Camera camera, World world) {
        super(Family.all(CharacterAnimationComponent.class, StateComponent.class).get());

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

    }

}