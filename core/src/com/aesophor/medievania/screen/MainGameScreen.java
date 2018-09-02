package com.aesophor.medievania.screen;

import box2dLight.RayHandler;
import com.aesophor.medievania.character.Character;
import com.aesophor.medievania.character.Player;
import com.aesophor.medievania.manager.GameMapManager;
import com.aesophor.medievania.manager.GameStateManager;
import com.aesophor.medievania.map.GameMap;
import com.aesophor.medievania.map.Portal;
import com.aesophor.medievania.map.WorldContactListener;
import com.aesophor.medievania.message.MessageArea;
import com.aesophor.medievania.ui.HUD;
import com.aesophor.medievania.util.CameraUtils;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Rumble;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class MainGameScreen extends AbstractScreen implements GameMapManager {

    private World world;
    private AssetManager assets;
    private RayHandler rayHandler;
    private Image shade;

    private OrthogonalTiledMapRenderer renderer;
    private Box2DDebugRenderer b2dr;
    private TmxMapLoader mapLoader;
    private GameMap currentMap;

    private final HUD hud;
    private final MessageArea messageArea;
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

        // Load the tiled map.
        mapLoader = new TmxMapLoader();
        load("Map/starting_point.tmx");

        // Initialize the OrthogonalTiledMapRenderer to show our map.
        renderer = new OrthogonalTiledMapRenderer(currentMap.getTiledMap(), 1 / Constants.PPM);
        b2dr = new Box2DDebugRenderer();

        // Initialize player and HUD.
        player = currentMap.spawnPlayer();
        hud = new HUD(gsm, player);
        messageArea = new MessageArea(gsm, 6, 3f);

        // Draw a shade over everything to provide fade in/out effects.
        shade = new Image(new TextureRegion(Utils.getTexture()));
        shade.setSize(getViewport().getScreenWidth(), getViewport().getScreenHeight());
        shade.setColor(0, 0, 0, 0);
        addActor(shade);
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

        if (player.isCrouching() && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.getUp();
        }

        // When player is attacking, movement is disabled.
        if (!player.isAttacking()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
                if (player.isCrouching()) {
                    player.jumpDown();
                } else {
                    player.jump();
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {

            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.moveLeft();
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.crouch();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                if (player.getCurrentPortal() != null && !player.isSetToKill()) {
                    shade.addAction(Actions.sequence(Actions.fadeIn(.3f), new RunnableAction() {
                        @Override
                        public void run() {
                            Portal currentPortal = player.getCurrentPortal();
                            int targetPortalID = currentPortal.getTargetPortalID();

                            load(currentPortal.getTargetMap());

                            // Reposition the player at the position of the target portal's body.
                            player.reposition(currentMap.getPortals().get(targetPortalID).getBody().getPosition());
                        }
                    }, Actions.fadeOut(.85f)));
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
        messageArea.update(delta);

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

        // Update all actors in this stage.
        this.act(delta);
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

        getBatch().setProjectionMatrix(messageArea.getCamera().combined);
        messageArea.draw();

        // Draw all actors in this stage.
        messageArea.draw();
        this.draw();
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
        if (shade != null) {
            shade.setSize(getCurrentMap().getMapWidth(), getCurrentMap().getMapHeight());
        }

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
        return mapLoader;
    }

    @Override
    public MessageArea getMessageArea() {
        return messageArea;
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

}