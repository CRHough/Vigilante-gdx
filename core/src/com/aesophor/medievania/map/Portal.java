package com.aesophor.medievania.map;

public class Portal {

    private GameMap destinationMap;

    public Portal(GameMap destinationMap) {
        this.destinationMap = destinationMap;
    }


    public GameMap getDestinationMap() {
        return destinationMap;
    }
}