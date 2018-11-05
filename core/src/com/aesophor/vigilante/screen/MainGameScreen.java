package com.aesophor.vigilante.screen;

import com.aesophor.vigilante.GameAssetManager;
import com.aesophor.vigilante.GameStateManager;
import com.aesophor.vigilante.entity.character.Player;
import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.screen.GamePausedEvent;
import com.aesophor.vigilante.event.screen.GameResumedEvent;
import com.aesophor.vigilante.event.screen.MainGameScreenResizeEvent;
import com.aesophor.vigilante.map.WorldContactListener;
import com.aesophor.vigilante.system.*;
import com.aesophor.vigilante.system.box2d.B2DebugRendererSystem;
import com.aesophor.vigilante.system.box2d.B2LightsSystem;
import com.aesophor.vigilante.system.box2d.PhysicsSystem;
import com.aesophor.vigilante.system.graphics.AnimatedSpriteRendererSystem;
import com.aesophor.vigilante.system.graphics.BodyRendererSystem;
import com.aesophor.vigilante.system.graphics.EquipmentRendererSystem;
import com.aesophor.vigilante.system.graphics.StaticSpriteRendererSystem;
import com.aesophor.vigilante.system.ui.*;
import com.aesophor.vigilante.ui.DamageIndicatorManager;
import com.aesophor.vigilante.ui.DialogManager;
import com.aesophor.vigilante.ui.NotificationManager;
import com.aesophor.vigilante.ui.component.Dialog;
import com.aesophor.vigilante.ui.hud.HUD;
import com.aesophor.vigilante.ui.pausemenu.PauseMenu;
import com.aesophor.vigilante.ui.theme.Font;
import com.aesophor.vigilante.util.Constants;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MainGameScreen extends AbstractScreen {

    private final GameEventManager gameEventManager;
    private final PooledEngine engine;
    private final Player player;
    private final World world;

    private final Sound pauseSound;

    public MainGameScreen(GameStateManager gsm) {
        super(gsm);

        // Since we will be rendering TiledMaps, we should scale the viewport with PPM.
        getViewport().setWorldSize(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM);

        // Initialize pooled engine.
        engine = new PooledEngine();

        // Initialize the GameEventManager.
        gameEventManager = GameEventManager.getInstance();

        // Initialize the world, and register the world contact listener.
        world = new World(new Vector2(0, Constants.GRAVITY), true);
        world.setContactListener(new WorldContactListener(engine, gsm.getAssets()));
        player = new Player(gsm.getAssets(), world, 0.3f, 1f);

        // Initialize damage indicators and notificationManager area.
        HUD hud = new HUD(gsm.getAssets(), gsm.getBatch());
        DialogManager dialogManager = new DialogManager(gsm);
        PauseMenu pauseMenu = new PauseMenu(gsm, player);
        DamageIndicatorManager damageIndicatorManager = new DamageIndicatorManager(getBatch(), Font.REGULAR, getCamera(), 1.2f);
        NotificationManager notificationManager = new NotificationManager(getBatch(), Font.REGULAR, 6, 4f);

        // Here I employ Entity-Component-System because it makes the layout of my code cleaner.
        // Tasks are independently spread into different systems/layers and can be added/removed on demand.
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new TiledMapRendererSystem((OrthographicCamera) getCamera()));     // Renders TiledMap textures.
        engine.addSystem(new BodyRendererSystem(getBatch(), getCamera(), world));           // Renders bodies of all characters.
        engine.addSystem(new EquipmentRendererSystem(getBatch(), getCamera(), world));      // Renders equipment of all characters.
        engine.addSystem(new AnimatedSpriteRendererSystem(engine, gsm.getAssets(), getBatch(), getCamera())); // Renders dust entities.
        engine.addSystem(new StaticSpriteRendererSystem(getBatch(), getCamera(), world));   // Renders item entities
        engine.addSystem(new B2DebugRendererSystem(world, getCamera()));                    // Renders physics debug profiles.
        engine.addSystem(new B2LightsSystem(world, getCamera()));                           // Renders Dynamic box2d lights.
        engine.addSystem(new CameraSystem(getCamera(), null, null));     // Camera shake / lerp to target.
        engine.addSystem(new PlayerControlSystem(engine));                                  // Handles player controls.
        engine.addSystem(new CharacterStatsSystem());                                       // Handles stats changes when equipment changes.
        engine.addSystem(new StatsRegenerationSystem());                                    // Stats regeneration (health...etc)
        engine.addSystem(new EnemyAISystem());                                              // Handles NPC behaviors.
        engine.addSystem(new GameMapManagementSystem(engine, gsm.getAssets(), world));      // Used to set current GameMap.
        engine.addSystem(new DamageIndicatorSystem(getBatch(), damageIndicatorManager));    // Renders damage indicators.
        engine.addSystem(new NotificationSystem(getBatch(), notificationManager));          // Renders Notifications.
        engine.addSystem(new HUDSystem(getBatch(), hud));                                   // Renders heads up display.
        engine.addSystem(new DialogSystem(engine, getBatch(), dialogManager));              // Renders DialogManager (dialogues).
        engine.addSystem(new PauseMenuSystem(getBatch(), pauseMenu));                       // Pause Menu.
        engine.addSystem(new ScreenFadeSystem(getBatch()));                                 // Renders screen fade effects.


        engine.addEntity(player);
        engine.getSystem(CameraSystem.class).registerPlayer(player);
        engine.getSystem(GameMapManagementSystem.class).registerPlayer(player);
        engine.getSystem(HUDSystem.class).registerPlayer(player);

        engine.getSystem(B2DebugRendererSystem.class).setProcessing(false);
        engine.getSystem(PauseMenuSystem.class).setProcessing(false);
        //engine.getSystem(DialogSystem.class).setProcessing(false);

        pauseSound = gsm.getAssets().get(GameAssetManager.OPEN_CLOSE_SOUND);

        Array<Dialog> dialogs = new Array<>();
        dialogs.add(new Dialog("Castle Guard", "How dare you trespass here!"));
        dialogs.add(new Dialog("Aesophor", "Get out of my way..."));
        dialogManager.show(dialogs);
    }


    public void update(float delta) {
        //Gdx.app.log("INFO", "Java Heap: " + (Gdx.app.getJavaHeap() / 1048576f));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!paused) {
                pause();
            } else {
                resume();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            EntitySystem b2drSys = engine.getSystem(B2DebugRendererSystem.class);
            b2drSys.setProcessing(!b2drSys.checkProcessing());
        }
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

        // Fire a screen-resize event to update the viewport of DamageIndicatorManager and RayHandler.
        int viewportX = getViewport().getScreenX();
        int viewportY = getViewport().getScreenY();
        int viewportWidth = getViewport().getScreenWidth();
        int viewportHeight = getViewport().getScreenHeight();
        gameEventManager.fireEvent(new MainGameScreenResizeEvent(viewportX, viewportY, viewportWidth, viewportHeight));
    }

    @Override
    public void pause() {
        super.pause();
        pauseSound.play();

        EntitySystem aiSys = engine.getSystem(EnemyAISystem.class);
        EntitySystem playerControlSys = engine.getSystem(PlayerControlSystem.class);
        EntitySystem physicsSys = engine.getSystem(PhysicsSystem.class);
        EntitySystem pauseMenuSys = engine.getSystem(PauseMenuSystem.class);

        aiSys.setProcessing(false);
        playerControlSys.setProcessing(false);
        physicsSys.setProcessing(false);
        pauseMenuSys.setProcessing(true);

        GameEventManager.getInstance().fireEvent(new GamePausedEvent());
    }

    @Override
    public void resume() {
        super.resume();
        pauseSound.play();

        EntitySystem aiSys = engine.getSystem(EnemyAISystem.class);
        EntitySystem playerControlSys = engine.getSystem(PlayerControlSystem.class);
        EntitySystem physicsSys = engine.getSystem(PhysicsSystem.class);
        EntitySystem pauseMenuSys = engine.getSystem(PauseMenuSystem.class);

        aiSys.setProcessing(true);
        playerControlSys.setProcessing(true);
        physicsSys.setProcessing(true);
        pauseMenuSys.setProcessing(false);

        GameEventManager.getInstance().fireEvent(new GameResumedEvent());
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

}