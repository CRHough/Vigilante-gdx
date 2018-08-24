package com.aesophor.medievania.screens;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.ui.Hud;
import com.aesophor.medievania.utils.WorldContactListener;
import com.aesophor.medievania.world.maps.Map;
import com.aesophor.medievania.world.objects.characters.Enemy;
import com.aesophor.medievania.world.objects.characters.humanoid.Knight;
import com.aesophor.medievania.world.objects.characters.humanoid.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    
    private Medievania game;
    private TextureAtlas atlas;
    
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Hud hud;
    
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    
    private World world;
    private Box2DDebugRenderer b2dr;
    
    private Player player;
    private Enemy enemy;
    
    private int mapWidth;
    private int mapHeight;
    
    private Music backgroundMusic;
    
    public GameScreen(Medievania game) {
        this.game = game;
        
        atlas = Medievania.manager.get("Character/Bandit/Bandit.pack");
        
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(Medievania.V_WIDTH / Medievania.PPM, Medievania.V_HEIGHT / Medievania.PPM, gameCamera);
        
        
        maploader = new TmxMapLoader();
        map = maploader.load("Map/starting_point.tmx");
        MapProperties prop = map.getProperties();
        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        
        
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Medievania.PPM);
        
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);
        
        world = new World(new Vector2(0, -10), true); // -10 is the gravity
        b2dr = new Box2DDebugRenderer();
        
        new Map(world, map);
        
        // Spawn the player.
        player = new Player(this, 32 / Medievania.PPM, 200 / Medievania.PPM);
        
        // Spawn an enemy.
        enemy = new Knight(this, 300 / Medievania.PPM, 100 / Medievania.PPM);
        
        world.setContactListener(new WorldContactListener(player));
        
        hud = new Hud(player, game.batch);
        
        // Play background music.
        backgroundMusic = Medievania.manager.get("Sound/Music/village01.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(.6f);
        //backgroundMusic.play();
    }

    
    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }
    
    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) && !player.isAttacking()) {
            player.setIsAttacking(true);
            
            if (player.hasTargetEnemy()) {
                player.attack(player.getTargetEnemy());
            }
            return;
        }
        
        // When player is attacking, movement is disabled.
        if (!player.isAttacking()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT) && !player.isJumping()) {
                player.setIsJumping(true);
                player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 0.5) {
                player.b2body.applyLinearImpulse(new Vector2(0.15f, 0), player.b2body.getWorldCenter(), true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -0.5) {
                player.b2body.applyLinearImpulse(new Vector2(-0.15f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }
    
    public void update(float dt) {
        handleInput(dt);
        
        world.step(1/60f, 6, 2);
        
        enemy.update(dt);
        player.update(dt);
        hud.update(dt);
        
        //gameCamera.position.x = player.b2body.getPosition().x;
        lerpToTarget(gameCamera, player.b2body.getPosition());
        
        float startX = gameCamera.viewportWidth / 2;
        float startY = gameCamera.viewportHeight / 2;
        float endX =  (mapWidth * 16) / Medievania.PPM - gameCamera.viewportWidth / 2;
        float endY = (mapHeight * 16) / Medievania.PPM - gameCamera.viewportHeight / 2;
        boundCamera(gameCamera, startX, startY, endX, endY);
        
        // Update our camera with correct coordinates after changes.
        gameCamera.update();
        
        // Tell our renderer to draw only what our camera can see.
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        update(delta);
        
        // Clear the game screen with pure black.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render our game map.
        renderer.render();
        
        // Render our Box2DDebugLines.
        //b2dr.render(world, gameCamera.combined);
        
        // Render our player.
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        
        enemy.draw(game.batch);
        player.draw(game.batch);
        
        game.batch.end();
        
        // Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        
        if (player.killed()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
    
    
    public TextureAtlas getAtlas() {
        return atlas;
    }
    
    public static void boundCamera(Camera camera, float startX, float startY, float endX, float endY) {
        Vector3 position = camera.position;
        
        if (position.x < startX) {
            position.x = startX;
        }
        if (position.y < startY) {
            position.y = startY;
        }
        
        if (position.x > endX) {
            position.x = endX;
        }
        if (position.y > endY) {
            position.y = endY;
        }
        
        camera.position.set(position);
        camera.update();
    }
    
    public static void lerpToTarget(Camera camera, Vector2 target) {
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * .1f;
        position.y = camera.position.y + (target.y - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }
    
    public World getWorld() {
        return world;
    }
    
    public SpriteBatch getBatch() {
        return game.batch;
    }
    

}