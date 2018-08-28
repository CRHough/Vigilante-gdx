package com.aesophor.medievania.screen;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.constants.Constants;
import com.aesophor.medievania.ui.HUD;
import com.aesophor.medievania.util.CameraUtils;
import com.aesophor.medievania.util.Rumble;
import com.aesophor.medievania.world.character.Enemy;
import com.aesophor.medievania.world.character.Player;
import com.aesophor.medievania.world.character.humanoid.Knight;
import com.aesophor.medievania.world.map.GameMap;
import com.aesophor.medievania.world.map.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class MainGame extends AbstractScreen {
    
    private OrthogonalTiledMapRenderer renderer;
    private TmxMapLoader maploader;
    private GameMap currentMap;
    
    private World world;
    private Box2DDebugRenderer b2dr;
    
    private HUD hud;
    
    private Player player;
    private Enemy enemy;
    
    private Music backgroundMusic;
    
    public MainGame(GameStateManager gameStateManager) {
        super(gameStateManager);
        
        // Since we will be rendering TiledMaps, we should scale the viewport with PPM.
        getViewport().setWorldSize(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM);
        
        world = new World(new Vector2(0, Constants.GRAVITY), true);
        b2dr = new Box2DDebugRenderer();
        
        maploader = new TmxMapLoader();
        currentMap = new GameMap(world, maploader.load("Map/starting_point.tmx"));
        renderer = new OrthogonalTiledMapRenderer(currentMap.getTiledMap(), 1 / Constants.PPM);
        
        
        // Spawn the player and an enemy.
        player = new Player(this, 30 / Constants.PPM, 200 / Constants.PPM);
        enemy = new Knight(this, 300 / Constants.PPM, 100 / Constants.PPM);
        
        world.setContactListener(new WorldContactListener(player));
        
        hud = new HUD(gameStateManager, player);
        
        // Play background music.
        backgroundMusic = gameStateManager.getAssets().get("Sound/FX/Environmental/water_dripping.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(.6f);
        backgroundMusic.play();
    }
    
    
    public void update(float delta) {
        player.handleInput(delta);
        
        world.step(1/60f, 6, 2);
        
        hud.update(delta);
        enemy.update(delta);
        player.update(delta);
        
        
        
        if (Rumble.getRumbleTimeLeft() > 0){
            CameraUtils.boundCamera(getCamera(), currentMap);
            Rumble.tick(Gdx.graphics.getDeltaTime());
            getCamera().translate(Rumble.getPos());
         } else {
             CameraUtils.lerpToTarget(getCamera(), player.getB2Body().getPosition());
             CameraUtils.boundCamera(getCamera(), currentMap);
        }
        
        
        
        // Tell our renderer to draw only what our camera can see.
        renderer.setView((OrthographicCamera) getCamera());
    }

    @Override
    public void render(float delta) {
        update(delta);
        
        gameStateManager.clearScreen();
        
        // Render game map.
        renderer.render();
        
        // Render our Box2DDebugLines.
        if (Constants.DEBUG == true) {
            b2dr.render(world, getCamera().combined);
        }
        
        // Render characters.
        getBatch().setProjectionMatrix(getCamera().combined);
        getBatch().begin();
        enemy.draw(getBatch());
        player.draw(getBatch());
        getBatch().end();
        
        // Set our batch to now draw what the Hud camera sees.
        getBatch().setProjectionMatrix(hud.getCamera().combined);
        hud.draw();
        
        /*
        if (player.isKilled()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        */
    }
    
    @Override
    public void dispose() {
        currentMap.getTiledMap().dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        backgroundMusic.dispose();
    }
    
    
    public GameStateManager getGSM() {
        return gameStateManager;
    }
    
    public World getWorld() {
        return world;
    }
    
}