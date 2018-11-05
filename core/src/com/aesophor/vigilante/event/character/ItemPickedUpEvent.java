package com.aesophor.vigilante.event.character;

import com.aesophor.vigilante.entity.item.Item;
import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

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