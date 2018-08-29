package com.aesophor.medievania.world.map;

import java.util.Map;
import java.util.HashMap;
import com.aesophor.medievania.world.map.GameMap;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import box2dLight.RayHandler;

public class GameMapManager implements Disposable {
    
    private TmxMapLoader maploader;
    private AssetManager assets;
    private World world;
    private RayHandler rayHandler;
    
    private GameMap currentMap;
    private Map<String, String> backgroundMusics;
    
    public GameMapManager(AssetManager assets, World world) {
        this.assets = assets;
        this.world = world;
        
        maploader = new TmxMapLoader();
        rayHandler = new RayHandler(world);
        
        // Refactor this later.
        backgroundMusics = new HashMap<>();
        backgroundMusics.put("Map/starting_point.tmx", "Sound/FX/Environmental/water_dripping.mp3");
    }
    
    
    /**
     * Loads the specified TiledMap.
     * @param mapFilePath path to the tiled map.
     */
    public void load(String mapFilePath) {
        if (currentMap != null) {
            currentMap.dispose();
        }
        
        TiledMap map = maploader.load(mapFilePath);
        Music music = assets.get(backgroundMusics.get(mapFilePath));
        
        currentMap = new GameMap(assets, world, rayHandler, map, music, .8f); // Temporary
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

    public RayHandler getRayHandler() {
        return rayHandler;
    }
    
    
    @Override
    public void dispose() {
        currentMap.dispose();
        world.dispose();
    }

}
