package com.aesophor.vigilante.system;

import com.aesophor.vigilante.component.Mappers;
import com.aesophor.vigilante.entity.character.Player;
import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.event.combat.InflictDamageEvent;
import com.aesophor.vigilante.event.map.MapChangedEvent;
import com.aesophor.vigilante.map.GameMap;
import com.aesophor.vigilante.util.CameraShake;
import com.aesophor.vigilante.util.CameraUtils;
import com.aesophor.vigilante.util.Constants;
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

        GameEventManager.getInstance().addEventListener(GameEventType.INFLICT_DAMAGE, (InflictDamageEvent e) -> {
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