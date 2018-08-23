package com.aesophor.medieval.scenes;

import com.aesophor.medieval.Medieval;
import com.aesophor.medieval.sprites.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    
    private static int barLength = 50; // pixel
    
    public Stage stage;
    private Viewport viewport; 
    
    private Player player;
    
    private Texture hudTexture;
    private TextureRegion barsBackground;
    private TextureRegion healthBar;
    private TextureRegion staminaBar;
    private TextureRegion magickaBar;
    
    private Image healthBarImage;
    private Image staminaBarImage;
    private Image magickaBarImage;
    
    public Hud(Player player, SpriteBatch sb) {
        this.player = player;
        
        viewport = new FitViewport(Medieval.V_WIDTH, Medieval.V_HEIGHT);
        stage = new Stage(viewport, sb);
        
        // Initializes player hud Texture and TextureRegions.
        hudTexture = Medieval.manager.get("Interface/HUD/hud.png");
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
        
        
        /*
        playerNameLabel = new Label("PLAYER", new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        healthLabel = new Label(String.format("HEALTH: %03d", player.getHealth()), new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        
        worldLabel = new Label(" ", new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        levelLabel = new Label(" ", new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        
        timeLabel = new Label("STARTING POINT", new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        //countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        */
        
        /*
        table.add(playerNameLabel).expandX().pad(10);
        table.add(worldLabel).expandX().pad(10);
        table.add(timeLabel).expandX().pad(10);
        table.row();
        table.add(healthLabel).expandX();
        table.add(levelLabel).expandX();
        //table.add(countdownLabel).expandX();
        */
        
        stage.addActor(hudTable);
        stage.addActor(barTable);
    }
    
    
    public void update(float dt) {
        healthBarImage.setScaleX(barLength * player.getHealth() / 100f); // 100 is only temporary (player's full heatlh is 100)
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    
}