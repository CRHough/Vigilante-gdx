package com.aesophor.medievania.system;

import com.aesophor.medievania.ui.NotificationArea;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class NotificationSystem extends EntitySystem {

    private final Batch batch;
    private final NotificationArea notificationArea;

    public NotificationSystem(Batch batch, NotificationArea notificationArea) {
        this.batch = batch;
        this.notificationArea = notificationArea;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(notificationArea.getCamera().combined);
        notificationArea.update(delta);
        notificationArea.draw();
    }

}