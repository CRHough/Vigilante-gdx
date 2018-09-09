package com.aesophor.medievania.system;

import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.map.MapChangedEvent;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TiledMapRendererSystem extends EntitySystem {

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;

    public TiledMapRendererSystem(OrthographicCamera camera) {
        this.camera = camera;
        renderer = new OrthogonalTiledMapRenderer(null, 1 / Constants.PPM);

        // Whenever the map has been changed, set the OrthogonalTiledMapRenderer to render our new map.
        GameEventManager.getInstance().addEventListener(GameEventType.MAP_CHANGED, (MapChangedEvent e) -> {
            renderer.setMap(e.getNewGameMap().getTiledMap());
        });
    }


    @Override
    public void update(float delta) {
        // Tell our renderer to draw only what our camera can see, and render the game map.
        renderer.setView(camera);
        renderer.render();
    }

}