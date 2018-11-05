package com.aesophor.vigilante.event.character;

import com.aesophor.vigilante.entity.item.Item;
import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class ItemDiscardedEvent extends GameEvent {

    private final Item item;

    public ItemDiscardedEvent(Item item) {
        super(GameEventType.ITEM_DISCARDED);
        this.item = item;
    }


    public Item getItem() {
        return item;
    }

}