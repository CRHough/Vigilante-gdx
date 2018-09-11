package com.aesophor.medievania.system;

import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.combat.ItemPickedUpEvent;
import com.aesophor.medievania.ui.NotificationFactory;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class NotificationSystem extends EntitySystem {

    private final Batch batch;
    private final NotificationFactory notificationFactory;

    public NotificationSystem(Batch batch, NotificationFactory notificationFactory) {
        this.batch = batch;
        this.notificationFactory = notificationFactory;

        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_PICKED_UP, (ItemPickedUpEvent e) -> {
            notificationFactory.show(String.format("You have gained an item. (%s)", e.getItem().toString()));
        });
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(notificationFactory.getCamera().combined);
        notificationFactory.update(delta);
        notificationFactory.draw();
    }

}