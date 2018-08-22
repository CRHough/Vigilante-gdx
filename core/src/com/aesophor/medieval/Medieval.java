package com.aesophor.medieval;

import com.aesophor.medieval.screens.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Medieval extends Game {
    
    public static final float PPM = 100; // Pixels per meter.
    public static final int V_WIDTH = 500;
    public static final int V_HEIGHT = 250;
    
    public static final short GROUND_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short MELEE_WEAPON_BIT = 128;
    
	public SpriteBatch batch;
	public static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		manager = new AssetManager();
		manager.load("Character/Bandit/Bandit.pack", TextureAtlas.class);
		manager.load("Character/Knight/Knight.pack", TextureAtlas.class);
		manager.load("Sound/Music/village01.mp3", Music.class);
        manager.load("Sound/FX/Player/hurt.wav", Sound.class);
        manager.load("Sound/FX/Player/death.mp3", Sound.class);
		manager.load("Sound/FX/Player/footstep.mp3", Music.class);
		manager.load("Sound/FX/knife-slash.mp3", Sound.class);
		manager.finishLoading();
		
		setScreen(new GameScreen(this));
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
