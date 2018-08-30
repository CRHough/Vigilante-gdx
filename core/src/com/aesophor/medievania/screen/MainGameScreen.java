package com.aesophor.medievania.screen;

import java.util.Map;
import java.util.HashMap;
import box2dLight.RayHandler;
import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.GameMapManager;
import com.aesophor.medievania.map.Portal;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.ui.HUD;
import com.aesophor.medievania.util.CameraUtils;
import com.aesophor.medievania.util.Rumble;
import com.aesophor.medievania.character.Character;
import com.aesophor.medievania.character.Player;
import com.aesophor.medievania.map.GameMap;
import com.aesophor.medievania.map.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MainGameScreen extends AbstractScreen implements GameMapManager {

    private AssetManager assets;
    private RayHandler rayHandler;
    private World world;

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


        world = new World(new Vector2(0, Constants.GRAVITY), true);
        assets = gsm.getAssets();
        rayHandler = new RayHandler(world);
        maploader = new TmxMapLoader();




        world.setContactListener(new WorldContactListener());
        load("Map/starting_point.tmx");

        // Spawn the player and NPCs from the map.
        // The maps contain information about where player and NPCs should be spawned.
        // Refactor this part later.

        player = currentMap.spawnPlayer();
        //enemies = currentMap.spawnNPCs(); // rename this later

        // Initialize HUD.
        hud = new HUD(gsm, player);

        renderer = new OrthogonalTiledMapRenderer(currentMap.getTiledMap(), 1 / Constants.PPM);
        b2dr = new Box2DDebugRenderer();



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
                if (player.getCurrentPortal() != null) {
                    Portal currentPortal = player.getCurrentPortal();
                    int targetPortalID = currentPortal.getTargetPortalID();

                    load(currentPortal.getTargetMap());
                    load(currentPortal.getTargetMap(), currentPortal.getTargetPortalID());

                    // Reposition the player at the position of the target portal's body.
                    player.reposition(currentMap.getPortals().get(targetPortalID).getBody().getPosition());
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

        // Render our Box2DDebugLines.
        if (Constants.DEBUG == true) {
            b2dr.render(world, getCamera().combined);
        }

        // Render characters.
        getBatch().setProjectionMatrix(getCamera().combined);
        getBatch().begin();
        enemies.forEach((Character c) -> c.draw(getBatch()));
        player.draw(getBatch());
        getBatch().end();

        rayHandler.setCombinedMatrix(getCamera().combined);
        rayHandler.render();

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


    public GameMap getCurrentMap() {
        return currentMap;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void load(String gameMapFile) {

        if (renderer != null && currentMap != null) {
            currentMap.getBackgroundMusic().stop();
            currentMap.dispose();
             //set the map in your renderer

            rayHandler.removeAll();

            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);

            for(int i = 0; i < bodies.size; i++)
            {
                System.out.println("destroying: " + bodies.get(i));

                if (!bodies.get(i).equals(player.getB2Body())) {
                    world.destroyBody(bodies.get(i));
                } else {
                    System.out.println("Detected player body. skipping...");
                }
            }
        }


        currentMap = new GameMap(this, gameMapFile); //load the new map
        currentMap.playBackgroundMusic();




        if (renderer != null)
            renderer.setMap(currentMap.getTiledMap());

        enemies = currentMap.spawnNPCs();


        //currentMap = new GameMap(this, gameMapFile, music, .8f); // Temporary
        //currentMap.playBackgroundMusic();

        // Spawn the player and NPCs from the map.
        // The maps contain information about where player and NPCs should be spawned.
        // Refactor this part later.
    }


    public void load(String gameMapFile, int portalOrdinal) {
        load(gameMapFile);
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

    public World getWorld() {
        return world;
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

}