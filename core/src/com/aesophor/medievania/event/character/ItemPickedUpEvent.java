package com.aesophor.medievania.event.character;

import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class ItemPickedUpEvent extends GameEvent {

    private final Item item;

    public ItemPickedUpEvent(Item item) {
        super(GameEventType.ITEM_PICKED_UP);
        this.item = item;
    }


    public Item getItem() {
        return item;
    }

}