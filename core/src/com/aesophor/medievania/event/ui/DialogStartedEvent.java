package com.aesophor.medievania.event.ui;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class DialogStartedEvent extends GameEvent {

    public DialogStartedEvent() {
        super(GameEventType.DIALOG_STARTED);
    }

}