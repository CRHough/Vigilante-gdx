package com.aesophor.medievania.event.ui;

import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class InventoryTabChangedEvent extends GameEvent {

    private final ItemType newTabItemType;

    public InventoryTabChangedEvent(ItemType newTabItemType) {
        super(GameEventType.INVENTORY_TAB_CHANGED);
        this.newTabItemType = newTabItemType;
    }


    public ItemType getNewTabItemType() {
        return newTabItemType;
    }

}