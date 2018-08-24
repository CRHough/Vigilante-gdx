package com.aesophor.medievania.screen;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.constant.Constants;
import com.aesophor.medievania.ui.Hud;
import com.aesophor.medievania.util.CameraUtils;
import com.aesophor.medievania.world.map.GameMap;
import com.aesophor.medievania.world.map.WorldContactListener;
import com.aesophor.medievania.world.object.character.Enemy;
import com.aesophor.medievania.world.object.character.Player;
import com.aesophor.medievania.world.object.character.humanoid.Knight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    
    private Medievania game;
    
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Hud hud;
    
    private OrthogonalTiledMapRenderer renderer;
    private TmxMapLoader maploader;
    private GameMap currentMap;
    
    private World world;
    private Box2DDebugRenderer b2dr;
    
    private Player player;
    private Enemy enemy;
    
    private Music backgroundMusic;
    
    public GameScreen(Medievania game) {
        this.game = game;
        
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, gameCamera);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);
        
        world = new World(new Vector2(0, Constants.GRAVITY), true); // -10 is the gravity
        b2dr = new Box2DDebugRenderer();
        
        maploader = new TmxMapLoader();
        currentMap = new GameMap(world, maploader.load("Map/starting_point.tmx"));
        renderer = new OrthogonalTiledMapRenderer(currentMap.getTiledMap(), 1 / Constants.PPM);
        
        
        // Spawn the player and an enemy.
        player = new Player(this, 32 / Constants.PPM, 200 / Constants.PPM);
        enemy = new Knight(this, 300 / Constants.PPM, 100 / Constants.PPM);
        
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
    
    public void update(float delta) {
        player.handleInput(delta);
        
        world.step(1/60f, 6, 2);
        
        hud.update(delta);
        enemy.update(delta);
        player.update(delta);
        
        CameraUtils.lockOnTarget(gameCamera, player.getB2Body().getPosition());
        CameraUtils.boundCamera(gameCamera, currentMap);
        
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
        b2dr.render(world, gameCamera.combined);
        
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
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
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
    
    
    
    public World getWorld() {
        return world;
    }
    
    public SpriteBatch getBatch() {
        return game.batch;
    }
    

}