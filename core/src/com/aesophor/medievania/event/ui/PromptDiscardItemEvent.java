package com.aesophor.medievania.event.ui;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class PromptDiscardItemEvent extends GameEvent {

    public PromptDiscardItemEvent() {
        super(GameEventType.PROMPT_DISCARD_ITEM);
    }

}