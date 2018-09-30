package com.aesophor.medievania.system.ui;

import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.ui.DialogEndedEvent;
import com.aesophor.medievania.event.ui.DialogStartedEvent;
import com.aesophor.medievania.screen.MainGameScreen;
import com.aesophor.medievania.ui.DialogBox;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class DialogSystem extends EntitySystem {

    private final Batch batch;
    private final DialogBox dialogBox;

    public DialogSystem(MainGameScreen gameScreen, Batch batch, DialogBox dialogBox) {
        this.batch = batch;
        this.dialogBox = dialogBox;

        GameEventManager.getInstance().addEventListener(GameEventType.DIALOG_STARTED, (DialogStartedEvent e) -> {
            gameScreen.freeze();
        });

        GameEventManager.getInstance().addEventListener(GameEventType.DIALOG_ENDED, (DialogEndedEvent e) -> {
            gameScreen.unfreeze();
        });
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(dialogBox.getCamera().combined);
        dialogBox.update(delta);
        dialogBox.draw();
    }

}