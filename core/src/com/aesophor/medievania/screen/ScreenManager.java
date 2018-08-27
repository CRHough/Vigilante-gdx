package com.aesophor.medievania.screen;

import com.aesophor.medievania.Medievania;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
    }
    
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