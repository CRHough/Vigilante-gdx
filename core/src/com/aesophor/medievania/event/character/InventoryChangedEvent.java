package com.aesophor.medievania.event.character;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class InventoryChangedEvent extends GameEvent {

    public InventoryChangedEvent() {
        super(GameEventType.INVENTORY_CHANGED);
    }

}