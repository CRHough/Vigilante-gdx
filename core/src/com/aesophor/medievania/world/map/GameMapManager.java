package com.aesophor.medievania.world.map;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameMapManager {
    
    private TmxMapLoader maploader;
    private GameMap currentMap;
    private World world;
    
    public GameMapManager(World world) {
        this.world = world;
        maploader = new TmxMapLoader();
    }
    
    
    /**
     * Loads the specified TiledMap.
     * @param mapFilePath path to the tiled map.
     */
    public void load(String mapFilePath) {
        if (currentMap != null) {
            currentMap.dispose();
        }
        currentMap = new GameMap(world, maploader.load("Map/starting_point.tmx"));
    }
    
    /**
     * Sets the world's gravity.
     * @param gravity world gravity.
     */
    public void setGravity(Vector2 gravity) {
        world.setGravity(gravity);
    }
    
    
    public GameMap getCurrentMap() {
        return currentMap;
    }
    
    public World getWorld() {
        return world;
    }

}
