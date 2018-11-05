package com.aesophor.vigilante.event.map;

import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.map.Portal;

public class PortalUsedEvent extends GameEvent {

    private final Portal portal;

    public PortalUsedEvent(Portal portal) {
        super(GameEventType.PORTAL_USED);
        this.portal = portal;
    }


    public Portal getPortal() {
        return portal;
    }

}