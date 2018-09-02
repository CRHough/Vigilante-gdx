package com.aesophor.medievania.screen;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen extends AbstractScreen {
    
    private static final String SKIN_FILE = "Interface/Skin/medievania_skin.json";
    private static final String BACKGROUND_TEXTURE_FILE = "Interface/mainmenu_bg.png";
    private static final String BACKGROUND_MUSIC_FILE = "Sound/Music";
    private static final String KEY_PRESS_SOUND_FILE = "Sound/FX/UI/click.wav";
    
    private Skin skin;
    private Texture backgroundTexture;
    
    private Music backgroundMusic;
    private Sound keyPressSound;
    
    private Label[] labels;
    private String[] menuItems;
    private int currentItem;
    
    public MainMenuScreen(Medievania gsm) {
        super(gsm);
        
        skin = gsm.getAssets().get(SKIN_FILE);
        backgroundTexture = gsm.getAssets().get(BACKGROUND_TEXTURE_FILE);
        keyPressSound = gsm.getAssets().get(KEY_PRESS_SOUND_FILE);
        
        menuItems = new String[] {"New Game", "Load Game", "Credits", "End"};
        labels = new Label[menuItems.length];
        
        Table labelTable = new Table();
        labelTable.bottom().right().padRight(30f).padBottom(30f);
        labelTable.setFillParent(true);
        
        for (int i = 0; i < menuItems.length; i++) {
            labels[i] = new Label(menuItems[i], skin);
            labelTable.add(labels[i]).padTop(5f).align(Align.right).row();
        }
        
        addActor(labelTable);
    }
    
    
    @Override
    public void render(float delta) {
        handleInput(delta);
        
        gsm.clearScreen();
        gsm.getBatch().begin();
        gsm.getBatch().draw(backgroundTexture, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        gsm.getBatch().end();
        
        for (int i = 0; i < labels.length; i++) {
            if (i == currentItem) labels[i].setColor(Color.RED);
            else labels[i].setColor(Color.WHITE);
        }
        
        draw();
    }
    
    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (currentItem > 0) {
                keyPressSound.play();
                currentItem--;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (currentItem < menuItems.length - 1) {
                keyPressSound.play();
                currentItem++;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            select();
        }
    }
    
    public void select() {
        switch (currentItem) {
            case 0:
                gsm.showScreen(Screens.GAME);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                Gdx.app.exit();
                break;
            default:
                Gdx.app.log("Main menu", "Unknown selected item. This shouldn't have happened!");
                break;
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        //backgroundMusic.dispose();
        keyPressSound.dispose();
    }

}