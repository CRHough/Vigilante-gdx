package com.aesophor.medievania.system;

import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.MainGameScreenResizeEvent;
import com.aesophor.medievania.ui.DamageIndicator;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class DamageIndicatorSystem extends EntitySystem {

    private final Batch batch;
    private final DamageIndicator damageIndicator;

    public DamageIndicatorSystem(Batch batch, DamageIndicator damageIndicator) {
        this.batch = batch;
        this.damageIndicator = damageIndicator;

        GameEventManager.getInstance().addEventListener(GameEventType.MAINGAME_SCREEN_RESIZED, (MainGameScreenResizeEvent e) -> {
            damageIndicator.getViewport().update(e.getViewportWidth(), e.getViewportHeight());
        });
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(damageIndicator.getCamera().combined);
        damageIndicator.update(delta);
        damageIndicator.draw();
    }

}