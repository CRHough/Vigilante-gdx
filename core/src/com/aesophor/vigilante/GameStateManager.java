package com.aesophor.vigilante;

import com.aesophor.vigilante.screen.Screens;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameStateManager {

    public void showScreen(Screens s);
    public void clearScreen();
    
    public SpriteBatch getBatch();
    public AssetManager getAssets();
    
}