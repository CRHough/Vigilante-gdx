package com.aesophor.medievania.system.ui;

import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.map.MapChangedEvent;
import com.aesophor.medievania.event.map.PortalUsedEvent;
import com.aesophor.medievania.ui.Shade;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ScreenFadeSystem extends EntitySystem {

    public static final float FADEIN_DURATION = .3f;
    public static final float FADEOUT_DURATION = .85f;

    private final Batch batch;
    private final Shade shade;

    public ScreenFadeSystem(Batch batch) {
        this.batch = batch;
        this.shade = new Shade(batch);

        // Fade in/out effects during map transitions.
        GameEventManager.getInstance().addEventListener(GameEventType.PORTAL_USED, (PortalUsedEvent e) -> {
            shade.addAction(Actions.fadeIn(FADEIN_DURATION));
        });

        GameEventManager.getInstance().addEventListener(GameEventType.MAP_CHANGED, (MapChangedEvent e) -> {
            shade.addAction(Actions.fadeOut(FADEOUT_DURATION));
        });
    }


    @Override
    public void update(float delta) {
        shade.act(delta);
        shade.draw();
    }

}