package com.aesophor.medievania.event.screen;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class GamePausedEvent extends GameEvent {

    public GamePausedEvent() {
        super(GameEventType.GAME_PAUSED);
    }

}