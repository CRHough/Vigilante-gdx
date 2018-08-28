package com.aesophor.medievania.world.map;

import com.aesophor.medievania.constants.Constants;
import com.aesophor.medievania.world.CategoryBits;
import com.aesophor.medievania.world.character.Player;
import com.aesophor.medievania.world.character.humanoid.Knight;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class GameMap implements Disposable {
    
    private static final boolean GROUND_COLLIDABLE = true;
    private static final boolean PLATFORM_COLLIDABLE = true;
    private static final boolean WALL_COLLIDABLE = true;
    private static final boolean CLIFF_MARKER_COLLIDABLE = false;
    
    private TiledMap tiledMap;
    private Music backgroundMusic;
    
    private int mapWidth;
    private int mapHeight;
    private int mapTileSize;
    
    public GameMap(AssetManager assets, World world, TiledMap tiledMap, Music backgroundMusic) {
        this.tiledMap = tiledMap;
        this.backgroundMusic = backgroundMusic;
        
        // Extract the properties from the map.
        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        assert tileWidth == tileHeight;
        mapTileSize = tileWidth;
        
        
        // Create bodies and fixtures from each layer.
        createPolylines(GameMapLayer.GROUND, world, GROUND_COLLIDABLE, Constants.GROUND_FRICTION, CategoryBits.GROUND);
        createRectangles(GameMapLayer.PLATFORM, world, PLATFORM_COLLIDABLE, Constants.GROUND_FRICTION, CategoryBits.PLATFORM);
        createPolylines(GameMapLayer.WALL, world, WALL_COLLIDABLE, 0, CategoryBits.WALL);
        createPolylines(GameMapLayer.CLIFF_MARKER, world, CLIFF_MARKER_COLLIDABLE, 0, CategoryBits.CLIFF_MARKER);
    }
    
    
    private void createRectangles(GameMapLayer layer, World world, boolean collidable, float friction, short categoryBits) {
        Body body;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        for (MapObject object : tiledMap.getLayers().get(layer.ordinal()).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            
            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(rect.getWidth() / 2 / Constants.PPM, rect.getHeight() / 2 / Constants.PPM);
            
            fdef.shape = polygonShape;
            fdef.isSensor = (!collidable);
            fdef.friction = friction;
            fdef.filter.categoryBits = categoryBits;
            body.createFixture(fdef);
            
            polygonShape.dispose();
        }
    }
    
    private void createPolylines(GameMapLayer layer, World world, boolean collidable, float friction, short categoryBits) {
        Body body;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        for (MapObject object : tiledMap.getLayers().get(layer.ordinal()).getObjects().getByType(PolylineMapObject.class)) {
            float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length / 2];
            
            for (int i = 0; i < worldVertices.length; i++) {
                worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
            }
            
            ChainShape chainShape = new ChainShape();
            chainShape.createChain(worldVertices);
            
            // We are drawing the polylines using the coordinates of their vertices,
            // so bdef should be set to zero.
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.setZero();
            body = world.createBody(bdef);
            
            fdef.shape = chainShape;
            fdef.isSensor = (!collidable);
            fdef.friction = friction;
            fdef.filter.categoryBits = categoryBits;
            body.createFixture(fdef);
            
            chainShape.dispose();
        }
    }
    
    public Player spawnPlayer(AssetManager assets, World world) {
        MapObject object = tiledMap.getLayers().get(GameMapLayer.PLAYER.ordinal()).getObjects().getByType(RectangleMapObject.class).get(0);
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        
        return new Player(assets, world, rect.getX() / Constants.PPM, rect.getY() / Constants.PPM);
    }
    
    public Array<Knight> spawnNPCs(AssetManager assets, World world) {
        Array<Knight> knights = new Array<>();
        
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