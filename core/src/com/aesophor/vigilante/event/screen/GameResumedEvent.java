package com.aesophor.vigilante.event.screen;

import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class GameResumedEvent extends GameEvent {

    public GameResumedEvent() {
        super(GameEventType.GAME_RESUMED);
    }

}