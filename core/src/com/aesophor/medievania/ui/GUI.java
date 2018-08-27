package com.aesophor.medievania.ui;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.constants.Constants;
import com.aesophor.medievania.screen.AbstractScreen;
import com.aesophor.medievania.util.CameraUtils;
import com.aesophor.medievania.world.object.character.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GUI extends AbstractScreen {
    
    private static int barLength = 50; // pixel
    
    private Player player;
    
    private Skin skin;
    private Texture hudTexture;
    private TextureRegion barsBackground;
    private TextureRegion healthBar;
    private TextureRegion staminaBar;
    private TextureRegion magickaBar;
    
    private Image healthBarImage;
    private Image staminaBarImage;
    private Image magickaBarImage;
    
    public GUI(Player player) {
        this.player = player;
        
        skin = screenMgr.getAssets().get("Interface/Skin/medievania_skin.json");
        
        // Initializes player hud Texture and TextureRegions.
        hudTexture = screenMgr.getAssets().get("Interface/HUD/hud.png");
        barsBackground = new TextureRegion(hudTexture, 0, 4, 100, 32);
        healthBar = new TextureRegion(hudTexture, 0, 0, 1, 4);
        staminaBar = new TextureRegion(hudTexture, 1, 0, 1, 4);
        magickaBar = new TextureRegion(hudTexture, 2, 0, 1, 4);
        
        
        Table hudTable = new Table();
        hudTable.top().left();
        hudTable.setFillParent(true);
        
        hudTable.add(new Image(barsBackground)).padTop(20f).padLeft(20f);
        
        
        Table barTable = new Table();
        barTable.top().left();
        barTable.setFillParent(true);
        barTable.padTop(24f).padLeft(58f);
        
        healthBarImage = new Image(healthBar);
        staminaBarImage = new Image(staminaBar);
        magickaBarImage = new Image(magickaBar);
        
        healthBarImage.setScaleX(barLength);
        staminaBarImage.setScaleX(barLength);
        magickaBarImage.setScaleX(barLength);
        
        barTable.add(healthBarImage);
        barTable.row().padTop(6f);
        barTable.add(staminaBarImage);
        barTable.row().padTop(6f);
        barTable.add(magickaBarImage);
        
        addActor(hudTable);
        addActor(barTable);
    }
    
    
    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            //inventoryImage
        }
    }
    
    public void update(float delta) {
        handleInput(delta);
    }


    @Override
    public void render(float delta) {
        update(delta);
        
        CameraUtils.clearScreen();
        
        getBatch().begin();
        //game.batch.draw(titlescreenTexture, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        getBatch().end();
        
        draw();
    }
    
}