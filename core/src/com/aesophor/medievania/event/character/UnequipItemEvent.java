package com.aesophor.medievania.event.character;

import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class UnequipItemEvent extends GameEvent {

    private final Item item;

    public UnequipItemEvent(Item item) {
        super(GameEventType.UNEQUIP_ITEM);
        this.item = item;
    }


    public Item getItem() {
        return item;
    }

}