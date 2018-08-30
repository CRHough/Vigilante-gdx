package com.aesophor.medievania.map;

import com.aesophor.medievania.GameMapManager;
import com.aesophor.medievania.character.Character;
import com.aesophor.medievania.character.Knight;
import com.aesophor.medievania.character.Player;
import com.aesophor.medievania.util.box2d.TiledObjectUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class GameMap implements Disposable {

    private GameMapManager gameMapManager;
    private String mapFilePath;
    private Music backgroundMusic;
    private Array<Portal> portals;
    private float brightness;

    private TiledMap tiledMap;
    private int mapWidth;
    private int mapHeight;
    private int mapTileSize;

    public GameMap(GameMapManager gameMapManager, String mapFilePath) {
        this.gameMapManager = gameMapManager;
        this.mapFilePath = mapFilePath;

        portals = new Array<>();

        tiledMap = gameMapManager.getMapLoader().load(mapFilePath);
        backgroundMusic = gameMapManager.getAssets().get((String) tiledMap.getProperties().get("backgroundMusic"));
        brightness = (Float) tiledMap.getProperties().get("brightness");

        
        // Extract the properties from the map.
        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        assert tileWidth == tileHeight;
        mapTileSize = tileWidth;

        // Update brightness according to this map.
        gameMapManager.getRayHandler().setAmbientLight(brightness);
        
        // Create bodies in the world according to each map layer.
        TiledObjectUtils.parseLayers(gameMapManager, this);
    }

    
    public Player spawnPlayer() {
        MapObject object = tiledMap.getLayers().get(GameMapLayer.PLAYER.ordinal()).getObjects().getByType(RectangleMapObject.class).get(0);
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        
        return new Player(gameMapManager, gameMapManager.getAssets(), gameMapManager.getWorld(), rect.getX(), rect.getY());
    }
    
    public Array<Character> spawnNPCs() {
        Array<Character> knights = new Array<>();
        
        for (MapObject object : tiledMap.getLayers().get(GameMapLayer.NPCS.ordinal()).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            knights.add(new Knight(gameMapManager.getAssets(), gameMapManager.getWorld(), rect.getX(), rect.getY()));
        }
        
        return knights;
    }
    
    
    public TiledMap getTiledMap() {
        return tiledMap;
    }
    
    public void playBackgroundMusic() {
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }
    
    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public Array<Portal> getPortals() {
        return portals;
    }
    
    public int getMapWidth() {
        return mapWidth;
    }
    
    public int getMapHeight() {
        return mapHeight;
    }
    
    public int getMapTileSize() {
        return mapTileSize;
    }


    @Override
    public String toString() {
        return mapFilePath;
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        backgroundMusic.dispose();
    }

}