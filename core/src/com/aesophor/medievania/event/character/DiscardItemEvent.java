package com.aesophor.medievania.event.character;

import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class DiscardItemEvent extends GameEvent {

    private final Item item;

    public DiscardItemEvent(Item item) {
        super(GameEventType.DISCARD_ITEM);
        this.item = item;
    }


    public Item getItem() {
        return item;
    }

}