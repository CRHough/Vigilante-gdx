package com.aesophor.vigilante.event.screen;

import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class GamePausedEvent extends GameEvent {

    public GamePausedEvent() {
        super(GameEventType.GAME_PAUSED);
    }

}