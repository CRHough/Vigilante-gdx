package com.aesophor.medievania.event.ui;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

/**
 * A DialogOptionEvent is used for creating anonymous game event listeners in MenuDialog.
 */
public class DialogOptionEvent extends GameEvent {

    public DialogOptionEvent() {
        super(GameEventType.DIALOG_OPTION);
    }

}