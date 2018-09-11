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

        assets.load("interface/skin/medievania_skin.json", Skin.class);
        assets.load("interface/mainmenu_bg.png", Texture.class);
        assets.load("interface/hud/hud.png", Texture.class);
        assets.load("character/bandit/Bandit.png", Texture.class);
        assets.load("character/knight/Knight.png", Texture.class);
        assets.load("item/RusticAxe.png", Texture.class);
        assets.load("music/main_menu.wav", Music.class);
        assets.load("music/village01.mp3", Music.class);
        assets.load("sfx/player/hurt.wav", Sound.class);
        assets.load("sfx/player/death.mp3", Sound.class);
        assets.load("sfx/player/weapon_swing.ogg", Sound.class);
        assets.load("sfx/player/weapon_hit.ogg", Sound.class);
        assets.load("sfx/player/pickup_item.mp3", Sound.class);
        assets.load("sfx/player/jump.wav", Sound.class);
        assets.load("sfx/player/footstep.mp3", Music.class);
        assets.load("sfx/knife-slash.mp3", Sound.class);
        assets.load("sfx/environment/water_dripping.mp3", Music.class);
        assets.load("sfx/ui/click.wav", Sound.class);
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
     * @return asset managers.
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