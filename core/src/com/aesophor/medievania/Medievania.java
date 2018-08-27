package com.aesophor.medievania;

import com.aesophor.medievania.screen.ScreenManager;
import com.aesophor.medievania.screen.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Medievania extends Game {
    
    private ScreenManager screenMgr;
    
    @Override
    public void create () {
        screenMgr = ScreenManager.getInstance();
        screenMgr.initialize(this, new SpriteBatch(), new AssetManager());
        
        screenMgr.getAssets().load("Interface/Skin/medievania_skin.json", Skin.class);
        screenMgr.getAssets().load("Interface/titlescreen.png", Texture.class);
        screenMgr.getAssets().load("Interface/HUD/hud.png", Texture.class);
        screenMgr.getAssets().load("Character/Bandit/Bandit.png", Texture.class);
        screenMgr.getAssets().load("Character/Knight/Knight.png", Texture.class);
        screenMgr.getAssets().load("Sound/Music/village01.mp3", Music.class);
        screenMgr.getAssets().load("Sound/FX/Player/hurt.wav", Sound.class);
        screenMgr.getAssets().load("Sound/FX/Player/death.mp3", Sound.class);
        screenMgr.getAssets().load("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        screenMgr.getAssets().load("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        screenMgr.getAssets().load("Sound/FX/Player/jump.wav", Sound.class);
        screenMgr.getAssets().load("Sound/FX/Player/footstep.mp3", Music.class);
        screenMgr.getAssets().load("Sound/FX/knife-slash.mp3", Sound.class);
        screenMgr.getAssets().load("Sound/FX/Environmental/water_dripping.mp3", Music.class);
        screenMgr.getAssets().finishLoading();
        
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
