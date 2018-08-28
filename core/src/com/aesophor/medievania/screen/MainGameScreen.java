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
import com.aesophor.medievania.world.map.GameMapManager;
import com.aesophor.medievania.world.map.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class MainGameScreen extends AbstractScreen {
    
    private GameMapManager gameMapManager;
    
    private OrthogonalTiledMapRenderer renderer;
    
    private Box2DDebugRenderer b2dr;
    
    private HUD hud;
    
    private Player player;
    private Enemy enemy;
    
    private Music backgroundMusic;
    
    public MainGameScreen(GameStateManager gsm) {
        super(gsm);
        
        // Since we will be rendering TiledMaps, we should scale the viewport with PPM.
        getViewport().setWorldSize(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM);
        b2dr = new Box2DDebugRenderer();
        
        gameMapManager = new GameMapManager(new World(new Vector2(0, Constants.GRAVITY), true));
        gameMapManager.load("Map/starting_point.tmx");
        gameMapManager.getWorld().setContactListener(new WorldContactListener());
        renderer = new OrthogonalTiledMapRenderer(gameMapManager.getCurrentMap().getTiledMap(), 1 / Constants.PPM);
        
        
        // Spawn the player and an enemy.
        player = new Player(this, 30 / Constants.PPM, 200 / Constants.PPM);
        enemy = new Knight(this, 300 / Constants.PPM, 100 / Constants.PPM);
        
        hud = new HUD(gsm, player);
        
        // Play background music.
        backgroundMusic = gsm.getAssets().get("Sound/FX/Environmental/water_dripping.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(.6f);
        backgroundMusic.play();
    }
    
    
    public void update(float delta) {
        player.handleInput(delta);
        
        gameMapManager.getWorld().step(1/60f, 6, 2);
        
        hud.update(delta);
        enemy.update(delta);
        player.update(delta);
        
        
        
        if (Rumble.getRumbleTimeLeft() > 0){
            CameraUtils.boundCamera(getCamera(), getCurrentMap());
            Rumble.tick(Gdx.graphics.getDeltaTime());
            getCamera().translate(Rumble.getPos());
         } else {
             CameraUtils.lerpToTarget(getCamera(), player.getB2Body().getPosition());
             CameraUtils.boundCamera(getCamera(), getCurrentMap());
        }
        
        
        
        // Tell our renderer to draw only what our camera can see.
        renderer.setView((OrthographicCamera) getCamera());
    }

    @Override
    public void render(float delta) {
        update(delta);
        
        gsm.clearScreen();
        
        // Render game map.
        renderer.render();
        
        // Render our Box2DDebugLines.
        if (Constants.DEBUG == true) {
            b2dr.render(gameMapManager.getWorld(), getCamera().combined);
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
        getCurrentMap().getTiledMap().dispose();
        renderer.dispose();
        getWorld().dispose();
        b2dr.dispose();
        hud.dispose();
        backgroundMusic.dispose();
    }
    
    
    public GameStateManager getGSM() {
        return gsm;
    }
    
    public GameMap getCurrentMap() {
        return gameMapManager.getCurrentMap();
    }
    
    public World getWorld() {
        return gameMapManager.getWorld();
    }
    
    
    
}