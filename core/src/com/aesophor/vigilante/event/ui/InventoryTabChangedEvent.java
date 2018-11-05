package com.aesophor.vigilante.event.ui;

import com.aesophor.vigilante.component.item.ItemType;
import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class InventoryTabChangedEvent extends GameEvent {

    private final ItemType newTabItemType;

    public InventoryTabChangedEvent(ItemType newTabItemType) {
        super(GameEventType.INVENTORY_TAB_SELECTED);
        this.newTabItemType = newTabItemType;
    }


    public ItemType getNewTabItemType() {
        return newTabItemType;
    }

}