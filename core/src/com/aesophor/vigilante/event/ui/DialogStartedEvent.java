package com.aesophor.vigilante.event.ui;

import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class DialogStartedEvent extends GameEvent {

    public DialogStartedEvent() {
        super(GameEventType.DIALOG_STARTED);
    }

}