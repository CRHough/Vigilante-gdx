package com.aesophor.medievania.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsSystem extends EntitySystem {

    private final World world;

    public PhysicsSystem(World world) {
        this.world = world;
    }


    @Override
    public void update(float delta) {
        world.step(1/60f, 6, 2);
    }

}