package com.aesophor.vigilante.event.ui;

import com.aesophor.vigilante.entity.item.Item;
import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class InventoryItemChangedEvent extends GameEvent {

    private final Item newItem;

    public InventoryItemChangedEvent(Item newItem) {
        super(GameEventType.INVENTORY_ITEM_SELECTED);
        this.newItem = newItem;
    }


    public Item getNewItem() {
        return newItem;
    }

}