package com.aesophor.medievania.event.character;

import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

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