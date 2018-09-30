package com.aesophor.medievania.system.ui;

import com.aesophor.medievania.ui.DialogBox;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MessageBoxSystem extends EntitySystem {

    private final Batch batch;
    private final DialogBox dialogBox;

    public MessageBoxSystem(Batch batch, DialogBox dialogBox) {
        this.batch = batch;
        this.dialogBox = dialogBox;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(dialogBox.getCamera().combined);
        dialogBox.update(delta);
        dialogBox.draw();
    }

}