package com.aesophor.medievania.event.screen;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class GameResumedEvent extends GameEvent {

    public GameResumedEvent() {
        super(GameEventType.GAME_RESUMED);
    }

}