package com.aesophor.vigilante.event.ui;

import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

/**
 * A MenuDialogOptionEvent is used for creating anonymous game event listeners in MenuDialog.
 */
public class MenuDialogOptionEvent extends GameEvent {

    public MenuDialogOptionEvent() {
        super(GameEventType.MENU_DIALOG_OPTION);
    }

}