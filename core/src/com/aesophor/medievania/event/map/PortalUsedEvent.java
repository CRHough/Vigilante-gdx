package com.aesophor.medievania.event.map;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.map.Portal;

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