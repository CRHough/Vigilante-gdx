package com.aesophor.vigilante.system.ui;

import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.event.combat.InflictDamageEvent;
import com.aesophor.vigilante.event.screen.MainGameScreenResizeEvent;
import com.aesophor.vigilante.ui.DamageIndicatorManager;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class DamageIndicatorSystem extends EntitySystem {

    private final Batch batch;
    private final DamageIndicatorManager damageIndicatorManager;

    public DamageIndicatorSystem(Batch batch, DamageIndicatorManager damageIndicatorManager) {
        this.batch = batch;
        this.damageIndicatorManager = damageIndicatorManager;

        GameEventManager.getInstance().addEventListener(GameEventType.MAINGAME_SCREEN_RESIZED, (MainGameScreenResizeEvent e) -> {
            damageIndicatorManager.getViewport().update(e.getViewportWidth(), e.getViewportHeight());
        });

        GameEventManager.getInstance().addEventListener(GameEventType.INFLICT_DAMAGE, (InflictDamageEvent e) -> {
            damageIndicatorManager.show(e.getTarget(), e.getDamage());
        });
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(damageIndicatorManager.getCamera().combined);
        damageIndicatorManager.update(delta);
        damageIndicatorManager.draw();
    }

}