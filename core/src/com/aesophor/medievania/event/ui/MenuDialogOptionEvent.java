package com.aesophor.medievania.event.ui;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

/**
 * A MenuDialogOptionEvent is used for creating anonymous game event listeners in MenuDialog.
 */
public class MenuDialogOptionEvent extends GameEvent {

    public MenuDialogOptionEvent() {
        super(GameEventType.MENU_DIALOG_OPTION);
    }

}