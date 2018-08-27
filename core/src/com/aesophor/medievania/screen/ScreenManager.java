package com.aesophor.medievania.screen;

import com.aesophor.medievania.Medievania;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ScreenManager {
    
    private static ScreenManager instance;
    private Medievania game;
    private SpriteBatch batch;
    private AssetManager assets;
    
    private ScreenManager() {
        super();
    }
    
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }
    
    
    public void initialize(Medievania game, SpriteBatch batch, AssetManager assets) {
        this.game = game;
        this.batch = batch;
        this.assets = assets;
        
        getAssets().load("Interface/Skin/medievania_skin.json", Skin.class);
        getAssets().load("Interface/mainmenu_bg.png", Texture.class);
        getAssets().load("Interface/HUD/hud.png", Texture.class);
        getAssets().load("Character/Bandit/Bandit.png", Texture.class);
        getAssets().load("Character/Knight/Knight.png", Texture.class);
        getAssets().load("Sound/Music/village01.mp3", Music.class);
        getAssets().load("Sound/FX/Player/hurt.wav", Sound.class);
        getAssets().load("Sound/FX/Player/death.mp3", Sound.class);
        getAssets().load("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        getAssets().load("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        getAssets().load("Sound/FX/Player/jump.wav", Sound.class);
        getAssets().load("Sound/FX/Player/footstep.mp3", Music.class);
        getAssets().load("Sound/FX/knife-slash.mp3", Sound.class);
        getAssets().load("Sound/FX/Environmental/water_dripping.mp3", Music.class);
        getAssets().load("Sound/FX/UI/click.wav", Sound.class);
        getAssets().finishLoading();
    }
    
    /**
     * Shows the specified GameScreen.
     * @param s screen to show.
     */
    public void showScreen(GameScreen s) {
        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();
 
        // Show new screen
        AbstractScreen newScreen = s.newScreen();
        //newScreen.buildStage();
        game.setScreen(newScreen);
 
        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
    
    /**
     * Clears the screen with pure black.
     */
    public void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    
    public void dispose() {
        assets.dispose();
        batch.dispose();
    }
    
    
    public Medievania getGame() {
        return game;
    }
    
    public SpriteBatch getBatch() {
        return batch;
    }
    
    public AssetManager getAssets() {
        return assets;
    }

}