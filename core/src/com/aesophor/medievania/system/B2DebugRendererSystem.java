package com.aesophor.medievania.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class B2DebugRendererSystem extends EntitySystem {

    private World world;
    private Camera camera;
    private Box2DDebugRenderer renderer;

    public B2DebugRendererSystem(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
        renderer = new Box2DDebugRenderer();
    }


    @Override
    public void update(float delta) {
        renderer.render(world, camera.combined);
    }

}