package com.aesophor.vigilante.system.ui;

import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.event.ui.DialogEndedEvent;
import com.aesophor.vigilante.event.ui.DialogStartedEvent;
import com.aesophor.vigilante.system.EnemyAISystem;
import com.aesophor.vigilante.system.PlayerControlSystem;
import com.aesophor.vigilante.ui.DialogManager;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Batch;

public class DialogSystem extends EntitySystem {

    private final Batch batch;
    private final DialogManager dialogManager;

    public DialogSystem(PooledEngine engine, Batch batch, DialogManager dialogManager) {
        this.batch = batch;
        this.dialogManager = dialogManager;

        GameEventManager.getInstance().addEventListener(GameEventType.DIALOG_STARTED, (DialogStartedEvent e) -> {
            onDialogStart(engine);
        });

        GameEventManager.getInstance().addEventListener(GameEventType.DIALOG_ENDED, (DialogEndedEvent e) -> {
            onDialogEnd(engine);
        });
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(dialogManager.getCamera().combined);
        dialogManager.update(delta);
        dialogManager.draw();
    }

    private void onDialogStart(PooledEngine engine) {
        EntitySystem aiSys = engine.getSystem(EnemyAISystem.class);
        EntitySystem playerControlSys = engine.getSystem(PlayerControlSystem.class);
        aiSys.setProcessing(false);
        playerControlSys.setProcessing(false);
    }

    private void onDialogEnd(PooledEngine engine) {
        EntitySystem aiSys = engine.getSystem(EnemyAISystem.class);
        EntitySystem playerControlSys = engine.getSystem(PlayerControlSystem.class);
        aiSys.setProcessing(true);
        playerControlSys.setProcessing(true);
    }

}