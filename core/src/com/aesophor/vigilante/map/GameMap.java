package com.aesophor.vigilante.map;

import com.aesophor.vigilante.entity.character.Character;
import com.aesophor.vigilante.entity.character.Enemy;
import com.aesophor.vigilante.entity.character.Player;
import com.aesophor.vigilante.util.Constants;
import com.aesophor.vigilante.util.box2d.TiledObjectUtils;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class GameMap implements Disposable {

    private final AssetManager assets;
    private final World world;

    private String mapFilePath;
    private TiledMap tiledMap;

    private Array<Portal> portals;
    private Music backgroundMusic;
    private float brightness;
    private int mapWidth;
    private int mapHeight;
    private int mapTileSize;

    public GameMap(AssetManager assets, World world, String mapFilePath) {
        this.assets = assets;
        this.world = world;
        this.mapFilePath = mapFilePath;

        tiledMap = TiledObjectUtils.tmxMapLoader.load(mapFilePath);
        portals = new Array<>();

        // Extract width, height and tile size from the map.
        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        assert tileWidth == tileHeight;
        mapTileSize = tileWidth;

        // Extract backgroundMusic and brightness from the map (they are stored as custom properties).
        backgroundMusic = assets.get((String) tiledMap.getProperties().get("backgroundMusic"));
        brightness = (Float) tiledMap.getProperties().get("brightness");

        // Create bodies in the world according to each map layer.
        TiledObjectUtils.parseLayers(world, this);
    }

    
    public Player spawnPlayer() {
        MapObject object = tiledMap.getLayers().get(GameMapLayer.PLAYER.ordinal()).getObjects().getByType(RectangleMapObject.class).get(0);
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        
        return new Player(assets, world, rect.getX() / Constants.PPM, rect.getY() / Constants.PPM);
    }
    
    public Array<Character> spawnNPCs() {
        Array<Character> npcs = new Array<>();
        
        for (MapObject object : tiledMap.getLayers().get(GameMapLayer.NPCS.ordinal()).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            String enemyName = (String) object.getProperties().get("name");
            npcs.add(new Enemy(enemyName, assets, world, rect.getX() / Constants.PPM, rect.getY() / Constants.PPM));
        }
        
        return npcs;
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

    public float getBrightness() {
        return brightness;
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
        assets.unload((String) tiledMap.getProperties().get("backgroundMusic"));
    }

}