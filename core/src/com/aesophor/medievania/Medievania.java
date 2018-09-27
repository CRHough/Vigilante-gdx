package com.aesophor.medievania;

import com.aesophor.medievania.entity.data.CharacterDataManager;
import com.aesophor.medievania.entity.data.EquipmentDataManager;
import com.aesophor.medievania.entity.data.ItemDataManager;
import com.aesophor.medievania.screen.AbstractScreen;
import com.aesophor.medievania.screen.Screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Medievania extends Game implements GameStateManager {
    
    private SpriteBatch batch;
    private AssetManager assets;
    
    @Override
    public void create () {
        this.batch = new SpriteBatch();
        this.assets = new Asset();

        ItemDataManager.getInstance().load("data/items.json");
        EquipmentDataManager.getInstance().load("data/equipment.json");
        CharacterDataManager.getInstance().load("data/characters.json");
        ((Asset) assets).loadAllAssets();
        assets.finishLoading();
        
        showScreen(Screens.MAIN_MENU);
    }
    
    
    /**
     * Shows the specified Screen.
     * @param s screen to update.
     */
    @Override
    public void showScreen(Screens s) {
        // Get current screen to dispose it
        Screen currentScreen = getScreen();
 
        // Show new screen
        AbstractScreen newScreen = s.newScreen(this);
        setScreen(newScreen);
 
        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
    
    /**
     * Clears the screen with pure black.
     */
    @Override
    public void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    
    
    /**
     * Gets the SpriteBatch.
     * @return sprite batch.
     */
    @Override
    public SpriteBatch getBatch() {
        return batch;
    }
    
    /**
     * Gets the AssetManager.
     * @return asset managers.
     */
    @Override
    public AssetManager getAssets() {
        return assets;
    }
    
    
    @Override
    public void render () {
        super.render();
        assets.update();
    }
    
    @Override
    public void dispose () {
        assets.dispose();
        batch.dispose();
    }
    
}