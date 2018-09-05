package com.aesophor.medievania.system;

import com.aesophor.medievania.util.CameraShake;
import com.aesophor.medievania.util.CameraUtils;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

public class GameCameraSystem extends EntitySystem {

    private final Camera camera;

    public GameCameraSystem(Camera camera) {
        this.camera = camera;
    }


    @Override
    public void update(float delta) {
        if (CameraShake.getShakeTimeLeft() > 0){
            CameraShake.update(Gdx.graphics.getDeltaTime());
            camera.translate(CameraShake.getPos());
        } else {
            CameraUtils.lerpToTarget(camera, player.getB2Body().getPosition());
        }

        // Make sure to bound the camera within the TiledMap.
        CameraUtils.boundCamera(camera, getCurrentMap());
    }

}