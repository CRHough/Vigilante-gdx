package com.aesophor.vigilante.system.ui;

import com.aesophor.vigilante.component.Mappers;
import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.event.combat.CharacterKilledEvent;
import com.aesophor.vigilante.event.character.ItemPickedUpEvent;
import com.aesophor.vigilante.ui.NotificationManager;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class NotificationSystem extends EntitySystem {

    private final Batch batch;
    private final NotificationManager notificationManager;

    public NotificationSystem(Batch batch, NotificationManager notificationManager) {
        this.batch = batch;
        this.notificationManager = notificationManager;

        GameEventManager.getInstance().addEventListener(GameEventType.CHARACTER_KILLED, (CharacterKilledEvent e) -> {
            int expGained = Mappers.STATS.get(e.getDeceased()).getExp();
            notificationManager.show(String.format("You have gained experience. (%d)", expGained));
        });

        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_PICKED_UP, (ItemPickedUpEvent e) -> {
            notificationManager.show(String.format("You have gained an item. (%s)", e.getItem().toString()));
        });
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(notificationManager.getCamera().combined);
        notificationManager.update(delta);
        notificationManager.draw();
    }

}