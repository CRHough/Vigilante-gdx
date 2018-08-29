package com.aesophor.medievania.world.map;

import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.box2d.LightBuilder;
import com.aesophor.medievania.util.box2d.TiledObjectUtils;
import com.aesophor.medievania.world.character.Character;
import com.aesophor.medievania.world.character.Player;
import com.aesophor.medievania.world.character.humanoid.Knight;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import box2dLight.RayHandler;

public class GameMap implements Disposable {
    
    private TiledMap tiledMap;
    private Music backgroundMusic;
    private float brightness;
    
    private int mapWidth;
    private int mapHeight;
    private int mapTileSize;
    
    public GameMap(AssetManager assets, World world, RayHandler rayHandler, TiledMap map, Music bgm, float brightness) {
        tiledMap = map;
        backgroundMusic = bgm;
        brightness = brightness;
        
        // Extract the properties from the map.
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);
        assert tileWidth == tileHeight;
        mapTileSize = tileWidth;

        // Update brightness according to this map.
        rayHandler.setAmbientLight(brightness);
        
        // Create bodies in the world according to each map layer.
        TiledObjectUtils.parseLayers(world, map, rayHandler);
    }

    
    public Player spawnPlayer(AssetManager assets, World world) {
        MapObject object = tiledMap.getLayers().get(GameMapLayer.PLAYER.ordinal()).getObjects().getByType(RectangleMapObject.class).get(0);
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        
        return new Player(assets, world, rect.getX(), rect.getY());
    }
    
    public Array<Character> spawnNPCs(AssetManager assets, World world) {
        Array<Character> knights = new Array<>();
        
        for (MapObject object : tiledMap.getLayers().get(GameMapLayer.NPCS.ordinal()).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            knights.add(new Knight(assets, world, rect.getX() / Constants.PPM, rect.getY() / Constants.PPM));
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
    public void dispose() {
        tiledMap.dispose();
        backgroundMusic.dispose();
    }

}