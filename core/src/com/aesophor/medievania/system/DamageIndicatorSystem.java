package com.aesophor.medievania.system;

import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.MainGameScreenResizeEvent;
import com.aesophor.medievania.ui.DamageIndicatorFactory;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class DamageIndicatorSystem extends EntitySystem {

    private final Batch batch;
    private final DamageIndicatorFactory damageIndicatorFactory;

    public DamageIndicatorSystem(Batch batch, DamageIndicatorFactory damageIndicatorFactory) {
        this.batch = batch;
        this.damageIndicatorFactory = damageIndicatorFactory;

        GameEventManager.getInstance().addEventListener(GameEventType.MAINGAME_SCREEN_RESIZED, (MainGameScreenResizeEvent e) -> {
            damageIndicatorFactory.getViewport().update(e.getViewportWidth(), e.getViewportHeight());
        });
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(damageIndicatorFactory.getCamera().combined);
        damageIndicatorFactory.update(delta);
        damageIndicatorFactory.draw();
    }

}