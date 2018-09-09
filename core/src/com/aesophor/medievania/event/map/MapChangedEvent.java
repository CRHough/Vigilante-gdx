package com.aesophor.medievania.event.map;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.map.GameMap;

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