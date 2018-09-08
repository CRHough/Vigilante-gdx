package com.aesophor.medievania.screens;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.GameWorldManager;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.MainGameScreenResizeEvent;
import com.aesophor.medievania.map.WorldContactListener;
import com.aesophor.medievania.system.*;
import com.aesophor.medievania.ui.DamageIndicatorFactory;
import com.aesophor.medievania.ui.NotificationArea;
import com.aesophor.medievania.ui.StatusBars;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class MainGameScreen extends AbstractScreen implements GameWorldManager {

    private final GameEventManager gameEventManager;
    private final PooledEngine engine;

    private final StatusBars statusBars;
    private final DamageIndicatorFactory damageIndicatorFactory;
    private final NotificationArea notificationArea;

    private final TmxMapLoader mapLoader;
    private final Player player;
    private World world;

    public MainGameScreen(GameStateManager gsm) {
        super(gsm);

        // Since we will be rendering TiledMaps, we should scale the viewport with PPM.
        getViewport().setWorldSize(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM);

        // Initialize the GameEventManager.
        gameEventManager = GameEventManager.getInstance();

        // Initialize the world, and register the world contact listener.
        world = new World(new Vector2(0, Constants.GRAVITY), true);
        world.setContactListener(new WorldContactListener());

        // Initialize damage indicators and notificationArea area.
        statusBars = new StatusBars(gsm);
        damageIndicatorFactory = new DamageIndicatorFactory(getBatch(), gsm.getFont().getDefaultFont(), getCamera(), 1.5f);
        notificationArea = new NotificationArea(getBatch(), gsm.getFont().getDefaultFont(), 6, 4f);

        // Load the map and spawn player.
        mapLoader = new TmxMapLoader();

        // Here I employ Entity-Component-System because it makes the layout of my code cleaner.
        // Tasks are independently spread into different systems/layers and can be added/removed on demand.
        engine = new PooledEngine();
        engine.addSystem(new TiledMapRendererSystem((OrthographicCamera) getCamera())); // Renders TiledMap textures.
        engine.addSystem(new CharacterRendererSystem(getBatch(), getCamera(), world));  // Renders entities (player/npcs/obj)
        engine.addSystem(new B2DebugRendererSystem(world, getCamera()));                // Renders physics debug profiles.
        engine.addSystem(new B2LightsSystem(world, getCamera()));                       // Renders Dynamic box2d lights.
        engine.addSystem(new CameraSystem(getCamera(), null, null)); // Camera shake / lerp to target.
        engine.addSystem(new PlayerControlSystem());                                    // Handles player controls.
        engine.addSystem(new EnemyAISystem());                                          // Handles NPC behaviors.
        engine.addSystem(new GameMapManagementSystem(this, world));   // Used to set current GameMap.
        engine.addSystem(new DamageIndicatorSystem(getBatch(), damageIndicatorFactory));       // Renders damage indicators.
        engine.addSystem(new NotificationSystem(getBatch(), notificationArea));         // Renders Notifications.
        engine.addSystem(new PlayerStatusBarsSystem(getBatch(), statusBars));           // Renders player status bars.
        engine.addSystem(new ScreenFadeSystem(getBatch()));                             // Renders screen fade effects.

        //player = getCurrentMap().spawnPlayer();
        player = new Player(this, 30, 100);
        engine.addEntity(player);

        engine.getSystem(CameraSystem.class).registerPlayer(player);
        engine.getSystem(GameMapManagementSystem.class).registerPlayer(player);
        engine.getSystem(PlayerStatusBarsSystem.class).registerPlayer(player);
    }


    public void update(float delta) {
        world.step(1/60f, 6, 2);
    }

    @Override
    public void render(float delta) {
        update(delta);
        gsm.clearScreen();
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        // Fire a screen-resize event to update the viewport of DamageIndicatorFactory and RayHandler.
        int viewportX = getViewport().getScreenX();
        int viewportY = getViewport().getScreenY();
        int viewportWidth = getViewport().getScreenWidth();
        int viewportHeight = getViewport().getScreenHeight();
        gameEventManager.fireEvent(new MainGameScreenResizeEvent(viewportX, viewportY, viewportWidth, viewportHeight));
    }

    @Override
    public void dispose() {
        //renderer.dispose();
        //b2dr.dispose();
        //statusBars.dispose();
        //currentMap.dispose();
        world.dispose();
        player.dispose();
        //npcs.forEach((Character c) -> c.dispose());
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public AssetManager getAssets() {
        return gsm.getAssets();
    }

    @Override
    public TmxMapLoader getMapLoader() {
        return mapLoader;
    }

    @Override
    public NotificationArea getNotificationArea() {
        return notificationArea;
    }

    @Override
    public DamageIndicatorFactory getDamageIndicatorFactory() {
        return damageIndicatorFactory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public PooledEngine getEngine() {
        return engine;
    }

}