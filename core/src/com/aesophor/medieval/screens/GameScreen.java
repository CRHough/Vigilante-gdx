package com.aesophor.medieval.screens;

import com.aesophor.medieval.Medieval;
import com.aesophor.medieval.scenes.Hud;
import com.aesophor.medieval.sprites.Enemy;
import com.aesophor.medieval.sprites.Knight;
import com.aesophor.medieval.sprites.Player;
import com.aesophor.medieval.util.B2WorldCreator;
import com.aesophor.medieval.util.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    
    private Medieval game;
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
    
    private Music backgroundMusic;
    
    public GameScreen(Medieval game) {
        this.game = game;
        
        atlas = Medieval.manager.get("Character/Bandit/Bandit.pack");
        
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(Medieval.V_WIDTH / Medieval.PPM, Medieval.V_HEIGHT / Medieval.PPM, gameCamera);
        
        
        maploader = new TmxMapLoader();
        map = maploader.load("Map/starting_point.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Medieval.PPM);
        
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);
        
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        
        new B2WorldCreator(world, map);
        
        // Spawn the player.
        player = new Player(world, this);
        
        // Spawn an enemy.
        enemy = new Knight(world, this, 100 / Medieval.PPM, 150 / Medieval.PPM);
        
        world.setContactListener(new WorldContactListener(player));
        
        hud = new Hud(player, game.batch);
        
        // Play background music.
        backgroundMusic = Medieval.manager.get("Sound/Music/village01.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    
    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }
    
    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) && !player.isAttacking()) {
            player.setIsAttacking(true);
            return;
        }
        
        // When player is attacking, movement is disabled.
        if (!player.isAttacking()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT) && !player.isJumping()) {
                player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 0.5) {
                player.b2body.applyLinearImpulse(new Vector2(0.07f, 0), player.b2body.getWorldCenter(), true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -0.5) {
                player.b2body.applyLinearImpulse(new Vector2(-0.07f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }
    
    public void update(float dt) {
        handleInput(dt);
        
        world.step(1/60f, 6, 2);
        
        enemy.update(dt);
        player.update(dt);
        hud.update(dt);
        
        gameCamera.position.x = player.b2body.getPosition().x + 100 / Medieval.PPM;
        
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

}
