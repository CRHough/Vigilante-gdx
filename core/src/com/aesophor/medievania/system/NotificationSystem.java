package com.aesophor.medievania.system;

import com.aesophor.medievania.ui.NotificationFactory;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class NotificationSystem extends EntitySystem {

    private final Batch batch;
    private final NotificationFactory notificationFactory;

    public NotificationSystem(Batch batch, NotificationFactory notificationFactory) {
        this.batch = batch;
        this.notificationFactory = notificationFactory;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(notificationFactory.getCamera().combined);
        notificationFactory.update(delta);
        notificationFactory.draw();
    }

}