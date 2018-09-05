package com.aesophor.medievania.event;

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