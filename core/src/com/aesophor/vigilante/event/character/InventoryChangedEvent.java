package com.aesophor.vigilante.event.character;

import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class InventoryChangedEvent extends GameEvent {

    public InventoryChangedEvent() {
        super(GameEventType.INVENTORY_CHANGED);
    }

}