package com.aesophor.medievania.system;

import com.aesophor.medievania.character.Player;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.MapChangedEvent;
import com.aesophor.medievania.map.GameMap;
import com.aesophor.medievania.util.CameraShake;
import com.aesophor.medievania.util.CameraUtils;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

public class CameraSystem extends EntitySystem {

    private Camera camera;
    private Player player;
    private GameMap currentMap;

    public CameraSystem(Camera camera, Player player, GameMap currentMap) {
        this.camera = camera;
        this.player = player;
        this.currentMap = currentMap;

        GameEventManager.getInstance().addEventListener(GameEventType.MAP_CHANGED, (MapChangedEvent e) -> {
            this.currentMap = e.getNewGameMap();
        });
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
        CameraUtils.boundCamera(camera, currentMap);
    }

    public void registerPlayer(Player player) {
        this.player = player;
    }


}