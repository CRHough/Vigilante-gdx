package com.aesophor.medievania.system.box2d;

import box2dLight.RayHandler;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.screen.MainGameScreenResizeEvent;
import com.aesophor.medievania.event.map.MapChangedEvent;
import com.aesophor.medievania.map.GameMapLayer;
import com.aesophor.medievania.util.box2d.TiledObjectUtils;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;

public class B2LightsSystem extends EntitySystem {

    private Camera camera;
    private RayHandler rayHandler;

    public B2LightsSystem(World world, Camera camera) {
        this.camera = camera;
        rayHandler = new RayHandler(world);

        // Whenever the map is changed, remove previous light objects and update brightness
        // according to the new map.
        GameEventManager.getInstance().addEventListener(GameEventType.MAP_CHANGED, (MapChangedEvent e) -> {
            rayHandler.removeAll();
            rayHandler.setAmbientLight(e.getNewGameMap().getBrightness());

            TiledObjectUtils.createLightSources(rayHandler, e.getNewGameMap(), GameMapLayer.LIGHT_SOURCE);
        });

        // Whenever the main game screen is resized, update custom viewport for the RayHandler.
        GameEventManager.getInstance().addEventListener(GameEventType.MAINGAME_SCREEN_RESIZED, (MainGameScreenResizeEvent e) -> {
            rayHandler.useCustomViewport(e.getViewportX(), e.getViewportY(), e.getViewportWidth(), e.getViewportHeight());
        });
    }


    @Override
    public void update(float delta) {
        // Render box2d lights.
        rayHandler.update();
        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.render();
    }

}