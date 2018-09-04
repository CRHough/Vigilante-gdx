package com.aesophor.medievania;

import com.aesophor.medievania.screen.AbstractScreen;
import com.aesophor.medievania.screen.Screens;
import com.aesophor.medievania.util.Font;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Medievania extends Game implements GameStateManager {
    
    private SpriteBatch batch;
    private AssetManager assets;
    private Font font;
    
    @Override
    public void create () {
        this.batch = new SpriteBatch();
        this.assets = new AssetManager();
        this.font = new Font(this);
        
        assets.load("Interface/Skin/medievania_skin.json", Skin.class);
        assets.load("Interface/mainmenu_bg.png", Texture.class);
        assets.load("Interface/HUD/hud.png", Texture.class);
        assets.load("Character/Bandit/Bandit.png", Texture.class);
        assets.load("Character/Knight/Knight.png", Texture.class);
        assets.load("Sound/Music/village01.mp3", Music.class);
        assets.load("Sound/FX/Player/hurt.wav", Sound.class);
        assets.load("Sound/FX/Player/death.mp3", Sound.class);
        assets.load("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        assets.load("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        assets.load("Sound/FX/Player/jump.wav", Sound.class);
        assets.load("Sound/FX/Player/footstep.mp3", Music.class);
        assets.load("Sound/FX/knife-slash.mp3", Sound.class);
        assets.load("Sound/FX/Environmental/water_dripping.mp3", Music.class);
        assets.load("Sound/FX/UI/click.wav", Sound.class);
        assets.finishLoading();
        
        showScreen(Screens.MAIN_MENU);
    }
    
    
    /**
     * Shows the specified Screen.
     * @param s screen to show.
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
     * @return asset manager.
     */
    @Override
    public AssetManager getAssets() {
        return assets;
    }

    /**
     * Gets the default font.
     * @return default font.
     */
    @Override
    public Font getFont() {
        return font;
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