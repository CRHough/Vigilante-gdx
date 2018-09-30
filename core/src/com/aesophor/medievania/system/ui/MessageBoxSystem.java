package com.aesophor.medievania.system.ui;

import com.aesophor.medievania.ui.MessageBox;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MessageBoxSystem extends EntitySystem {

    private final Batch batch;
    private final MessageBox messageBox;

    public MessageBoxSystem(Batch batch, MessageBox messageBox) {
        this.batch = batch;
        this.messageBox = messageBox;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(messageBox.getCamera().combined);
        messageBox.update(delta);
        messageBox.draw();
    }

}