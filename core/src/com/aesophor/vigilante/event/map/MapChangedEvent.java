package com.aesophor.vigilante.event.map;

import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.map.GameMap;

public class MapChangedEvent extends GameEvent {

    private GameMap newGameMap;

    public MapChangedEvent(GameMap newGameMap) {
        super(GameEventType.MAP_CHANGED);
        this.newGameMap = newGameMap;
    }


    public GameMap getNewGameMap() {
        return newGameMap;
    }

}