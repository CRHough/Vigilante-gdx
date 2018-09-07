package com.aesophor.medievania.system;

import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.MapChangedEvent;
import com.aesophor.medievania.event.PortalUsedEvent;
import com.aesophor.medievania.util.Utils;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ScreenFadeSystem extends EntitySystem {

    public static final float FADEIN_DURATION = .3f;
    public static final float FADEOUT_DURATION = .85f;

    private Stage mainGameStage;
    private final Image shade;

    public ScreenFadeSystem(Stage mainGameStage) {
        this.mainGameStage = mainGameStage;

        // Initialize shade to provide fade in/out effects later.
        // The shade is drawn atop everything, with only its transparency being adjusted.
        shade = new Image(new TextureRegion(Utils.getTexture()));
        shade.setSize(mainGameStage.getViewport().getScreenWidth(), mainGameStage.getViewport().getScreenHeight());
        shade.setColor(0, 0, 0, 0);
        mainGameStage.addActor(shade);

        // Update shade size to make fade in/out cover entire GameMap, and then fade in and out.
        GameEventManager.getInstance().addEventListener(GameEventType.PORTAL_USED, (PortalUsedEvent e) -> {
            shade.addAction(Actions.fadeIn(FADEIN_DURATION));
        });

        GameEventManager.getInstance().addEventListener(GameEventType.MAP_CHANGED, (MapChangedEvent e) -> {
            shade.setSize(e.getNewGameMap().getMapWidth(), e.getNewGameMap().getMapHeight());
            shade.addAction(Actions.fadeOut(FADEOUT_DURATION));
        });
    }


    @Override
    public void update(float delta) {
        mainGameStage.act(delta);
        mainGameStage.draw();
    }

}