package com.aesophor.medievania.screen;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.constants.Constants;
import com.aesophor.medievania.ui.HUD;
import com.aesophor.medievania.util.CameraUtils;
import com.aesophor.medievania.util.Rumble;
import com.aesophor.medievania.world.character.Player;
import com.aesophor.medievania.world.character.humanoid.Knight;
import com.aesophor.medievania.world.map.GameMap;
import com.aesophor.medievania.world.map.GameMapManager;
import com.aesophor.medievania.world.map.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MainGameScreen extends AbstractScreen {
    
    private GameMapManager gameMapManager;
    private OrthogonalTiledMapRenderer renderer;
    private Box2DDebugRenderer b2dr;
    
    private HUD hud;
    private Player player;
    private Array<Knight> npcs;
    
    public MainGameScreen(GameStateManager gsm) {
        super(gsm);
        
        // Since we will be rendering TiledMaps, we should scale the viewport with PPM.
        getViewport().setWorldSize(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM);
        
        // Initialize GameMapManager and load the default map (temporary).
        gameMapManager = new GameMapManager(gsm.getAssets(), new World(new Vector2(0, Constants.GRAVITY), true));
        gameMapManager.load("Map/starting_point.tmx");
        gameMapManager.getWorld().setContactListener(new WorldContactListener());
        gameMapManager.getCurrentMap().playBackgroundMusic();
        
        // Spawn the player and NPCs from the map.
        // The maps contain information about where player and NPCs should be spawned.
        player = gameMapManager.getCurrentMap().spawnPlayer(gsm.getAssets(), getWorld());
        npcs = gameMapManager.getCurrentMap().spawnNPCs(gsm.getAssets(), getWorld());
        
        // Initialize OrthogonalTiledMapRenderer and Box2DDebugRenderer.
        renderer = new OrthogonalTiledMapRenderer(gameMapManager.getCurrentMap().getTiledMap(), 1 / Constants.PPM);
        b2dr = new Box2DDebugRenderer();
        
        // Initialize HUD.
        hud = new HUD(gsm, player);
    }
    
    
    public void update(float delta) {
        player.handleInput(delta);
        
        gameMapManager.getWorld().step(1/60f, 6, 2);
        
        
        for (Knight c : npcs) {
            c.update(delta);
        }
        
        player.update(delta);

        hud.update(delta);
        
        
        
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
        for (Knight c : npcs) {
            c.draw(getBatch());
        }
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
        renderer.dispose();
        b2dr.dispose();
        hud.dispose();
        gameMapManager.dispose();
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