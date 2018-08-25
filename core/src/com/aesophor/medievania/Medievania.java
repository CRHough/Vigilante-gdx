package com.aesophor.medievania;

import com.aesophor.medievania.screen.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Medievania extends Game {
    
    private static final Medievania INSTANCE = new Medievania();
    public static SpriteBatch batch;
    public static AssetManager manager;
    
    private Medievania() {
        
    }
    
    public static Medievania getInstance() {
        return INSTANCE;
    }
    
	
	
	@Override
	public void create () {
	    batch = new SpriteBatch();
        manager = new AssetManager();
        
        manager.load("Interface/titlescreen.png", Texture.class);
		manager.load("Interface/HUD/hud.png", Texture.class);
		manager.load("Character/Bandit/Bandit.png", Texture.class);
		manager.load("Character/Knight/Knight.png", Texture.class);
		manager.load("Sound/Music/village01.mp3", Music.class);
        manager.load("Sound/FX/Player/hurt.wav", Sound.class);
        manager.load("Sound/FX/Player/death.mp3", Sound.class);
        manager.load("Sound/FX/Player/weapon_swing.ogg", Sound.class);
        manager.load("Sound/FX/Player/weapon_hit.ogg", Sound.class);
        manager.load("Sound/FX/Player/jump.wav", Sound.class);
		manager.load("Sound/FX/Player/footstep.mp3", Music.class);
		manager.load("Sound/FX/knife-slash.mp3", Sound.class);
		manager.finishLoading();
		
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
	    super.render();
	    manager.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}
}
