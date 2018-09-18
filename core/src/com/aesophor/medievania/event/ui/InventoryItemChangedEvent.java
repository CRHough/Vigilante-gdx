package com.aesophor.medievania.event.ui;

import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class InventoryItemChangedEvent extends GameEvent {

    private final Item newItem;

    public InventoryItemChangedEvent(Item newItem) {
        super(GameEventType.INVENTORY_ITEM_CHANGED);
        this.newItem = newItem;
    }


    public Item getNewItem() {
        return newItem;
    }

}