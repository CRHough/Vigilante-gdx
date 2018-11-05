package com.aesophor.vigilante.event.ui;

import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class DialogEndedEvent extends GameEvent {

    public DialogEndedEvent() {
        super(GameEventType.DIALOG_ENDED);
    }

}