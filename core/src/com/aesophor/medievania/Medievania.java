package com.aesophor.medievania;

import com.aesophor.medievania.screen.GameScreen;
import com.aesophor.medievania.screen.ScreenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Medievania extends Game {
    
    private ScreenManager screenMgr;
    
    @Override
    public void create () {
        screenMgr = ScreenManager.getInstance();
        screenMgr.initialize(this, new SpriteBatch(), new AssetManager());
        screenMgr.showScreen(GameScreen.MAIN_MENU);
    }

    @Override
    public void render () {
        super.render();
        screenMgr.getAssets().update();
    }
    
    @Override
    public void dispose () {
        screenMgr.dispose();
    }
    
}