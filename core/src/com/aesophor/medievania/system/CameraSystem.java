package com.aesophor.medievania.system;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.combat.InflictDamageEvent;
import com.aesophor.medievania.event.map.MapChangedEvent;
import com.aesophor.medievania.map.GameMap;
import com.aesophor.medievania.util.CameraShake;
import com.aesophor.medievania.util.CameraUtils;
import com.aesophor.medievania.util.Constants;
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

        GameEventManager.getInstance().addEventListener(GameEventType.HEALTH_CHANGED, (InflictDamageEvent e) -> {
            if (e.getSource().equals(this.player) || e.getTarget().equals(this.player)) {
                CameraShake.shake(8 / Constants.PPM, .1f);
            }
        });
    }


    @Override
    public void update(float delta) {
        if (CameraShake.getShakeTimeLeft() > 0){
            CameraShake.update(Gdx.graphics.getDeltaTime());
            camera.translate(CameraShake.getPos());
        } else {
            CameraUtils.lerpToTarget(camera, Mappers.B2BODY.get(player).getBody().getPosition());
        }

        // Make sure to bound the camera within the TiledMap.
        CameraUtils.boundCamera(camera, currentMap);
    }

    public void registerPlayer(Player player) {
        this.player = player;
    }

}