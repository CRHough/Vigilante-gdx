package com.aesophor.medievania.system.ui;

import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.ui.DialogEndedEvent;
import com.aesophor.medievania.event.ui.DialogStartedEvent;
import com.aesophor.medievania.system.EnemyAISystem;
import com.aesophor.medievania.system.PlayerControlSystem;
import com.aesophor.medievania.ui.DialogManager;
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