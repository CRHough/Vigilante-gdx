package com.aesophor.medievania.event.ui;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class DialogEndedEvent extends GameEvent {

    public DialogEndedEvent() {
        super(GameEventType.DIALOG_ENDED);
    }

}