package com.aesophor.medievania.screen;

import box2dLight.RayHandler;
import com.aesophor.medievania.GameMapManager;
import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.character.Character;
import com.aesophor.medievania.character.Player;
import com.aesophor.medievania.map.GameMap;
import com.aesophor.medievania.map.Portal;
import com.aesophor.medievania.map.WorldContactListener;
import com.aesophor.medievania.ui.HUD;
import com.aesophor.medievania.util.CameraUtils;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Rumble;
import com.aesophor.medievania.util.ScreenEffects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class MainGameScreen extends AbstractScreen implements GameMapManager {

    private World world;
    private AssetManager assets;
    private RayHandler rayHandler;
    private ScreenEffects effects;

    private OrthogonalTiledMapRenderer renderer;
    private Box2DDebugRenderer b2dr;
    private TmxMapLoader maploader;
    private GameMap currentMap;

    private HUD hud;
    private Player player;
    private Array<Character> enemies;

    public MainGameScreen(GameStateManager gsm) {
        super(gsm);

        // Since we will be rendering TiledMaps, we should scale the viewport with PPM.
        getViewport().setWorldSize(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM);

        // Initialize the world.
        world = new World(new Vector2(0, Constants.GRAVITY), true);
        world.setContactListener(new WorldContactListener());

        assets = gsm.getAssets();
        rayHandler = new RayHandler(world);
        effects = new ScreenEffects(getBatch(), getCamera());

        // Load the tiled map.
        maploader = new TmxMapLoader();
        load("Map/starting_point.tmx");

        // Initialize the OrthogonalTiledMapRenderer to show our map.
        renderer = new OrthogonalTiledMapRenderer(currentMap.getTiledMap(), 1 / Constants.PPM);
        b2dr = new Box2DDebugRenderer();

        // Initialize player and HUD.
        player = currentMap.spawnPlayer();
        hud = new HUD(gsm, player);
    }


    public void handleInput(float delta) {
        if (player.isSetToKill()) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            Constants.DEBUG = (Constants.DEBUG) ? false : true;
        }


        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            player.swingWeapon();
        }

        // When player is attacking, movement is disabled.
        if (!player.isAttacking()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
                player.jump();
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                player.getB2Body().getPosition().y += 5;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                // if (!isCrouching) crouch();
                // else getUp();
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.moveLeft();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                if (player.getCurrentPortal() != null && !player.isSetToKill()) {
                    effects.fadeOut(.5f);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            Portal currentPortal = player.getCurrentPortal();
                            int targetPortalID = currentPortal.getTargetPortalID();

                            load(currentPortal.getTargetMap());
                            effects.fadeIn(.7f);

                            // Reposition the player at the position of the target portal's body.
                            player.reposition(currentMap.getPortals().get(targetPortalID).getBody().getPosition());
                        }
                    }, .75f);
                }
            }
        }
    }

    public void update(float delta) {
        handleInput(delta);

        world.step(1/60f, 6, 2);
        rayHandler.update();

        enemies.forEach((Character c) -> c.update(delta));
        player.update(delta);
        hud.update(delta);

        if (Rumble.getRumbleTimeLeft() > 0){
            Rumble.tick(Gdx.graphics.getDeltaTime());
            getCamera().translate(Rumble.getPos());
        } else {
            CameraUtils.lerpToTarget(getCamera(), player.getB2Body().getPosition());
        }

        // Make sure to bound the camera within the TiledMap.
        CameraUtils.boundCamera(getCamera(), getCurrentMap());

        // Tell our renderer to draw only what our camera can see.
        renderer.setView((OrthographicCamera) getCamera());
    }

    @Override
    public void render(float delta) {
        update(delta);
        gsm.clearScreen();

        // Render game map.
        renderer.render();
        if (Constants.DEBUG == true) b2dr.render(world, getCamera().combined);

        // Render box2d lights.
        rayHandler.setCombinedMatrix(getCamera().combined);
        rayHandler.render();

        // Render characters.
        getBatch().setProjectionMatrix(getCamera().combined);
        getBatch().begin();
        enemies.forEach((Character c) -> c.draw(getBatch()));
        player.draw(getBatch());
        getBatch().end();

        // Set our batch to now draw what the Hud camera sees.
        getBatch().setProjectionMatrix(hud.getCamera().combined);
        hud.draw();

        // Draws a shade over the entire screen so that we can provide fade out/in effects later.
        effects.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        int viewportX = getViewport().getScreenX();
        int viewportY = getViewport().getScreenY();
        int viewportWidth = getViewport().getScreenWidth();
        int viewportHeight = getViewport().getScreenHeight();
        rayHandler.useCustomViewport(viewportX, viewportY, viewportWidth, viewportHeight);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        b2dr.dispose();
        hud.dispose();
        currentMap.dispose();
        world.dispose();
        player.dispose();
        enemies.forEach((Character c) -> c.dispose());
    }


    @Override
    public void load(String gameMapFile) {
        // Dispose previous map data. Don't execute this block if it is the first time loading a map.
        if (renderer != null && currentMap != null) {
            // Stop the background music, lights and dispose previous GameMap.
            currentMap.getBackgroundMusic().stop();
            rayHandler.removeAll();
            currentMap.dispose();

            // Destroy all bodies except player's body.
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);

            for(int i = 0; i < bodies.size; i++)
            {
                if (!bodies.get(i).equals(player.getB2Body())) {
                    world.destroyBody(bodies.get(i));
                }
            }
        }

        // Load the new map from gameMapFile.
        currentMap = new GameMap(this, gameMapFile);
        currentMap.playBackgroundMusic();

        // Sets the OrthogonalTiledMapRenderer to show our new map.
        if (renderer != null) {
            renderer.setMap(currentMap.getTiledMap());
        }

        // Update shade size to make fade out/in work correctly.
        effects.updateShadeSize(getCurrentMap().getMapWidth(), getCurrentMap().getMapHeight());

        // TODO: Don't respawn enemies whenever a map loads.
        enemies = currentMap.spawnNPCs();
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public AssetManager getAssets() {
        return assets;
    }

    @Override
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    @Override
    public TmxMapLoader getMapLoader() {
        return maploader;
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

}