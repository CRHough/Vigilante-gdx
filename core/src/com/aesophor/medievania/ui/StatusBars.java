package com.aesophor.medievania.ui;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.StatsComponent;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Font;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class StatusBars extends Stage {

    private static int barLength = 75; // pixel

    private StatsComponent playerStats;
    
    private Texture hudTexture;
    private TextureRegion barsBackground;
    private TextureRegion barsPadRight;
    private TextureRegion healthBar;
    private TextureRegion staminaBar;
    private TextureRegion magickaBar;
    private TextureRegion extraItem;

    private Image healthBarImage;
    private Image healthBarPadImage;
    private Image staminaBarImage;
    private Image staminaBarPadImage;
    private Image magickaBarImage;
    private Image magickaBarPadImage;

    private Image extraItemImageOne;
    private Image extraItemImageTwo;

    private Texture weaponIcon;
    private Image weaponIconImage;
    private Label weaponNameLabel;
    
    private Table hudTable;
    private Table barTable;
    private Table extraTable;
    private Table textTable;

    public StatusBars(GameStateManager gsm) {
        this(gsm, null);
    }
    
    public StatusBars(GameStateManager gsm, Player player) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());

        if (player != null) {
            playerStats = Mappers.STATS.get(player);
        }
        
        // Initializes player hud Texture and TextureRegions.
        hudTexture = gsm.getAssets().get("interface/hud/hud.png");
        barsBackground = new TextureRegion(hudTexture, 0, 4, 135, 50);
        barsPadRight = new TextureRegion(hudTexture, 3, 0, 1, 4);
        healthBar = new TextureRegion(hudTexture, 0, 0, 1, 4);
        staminaBar = new TextureRegion(hudTexture, 1, 0, 1, 4);
        magickaBar = new TextureRegion(hudTexture, 2, 0, 1, 4);
        extraItem = new TextureRegion(hudTexture, 0, 54, 65, 15);
        weaponIcon = gsm.getAssets().get("item/RusticAxe.png");
        
        
        hudTable = new Table();
        hudTable.top().left();
        hudTable.setFillParent(true);
        
        hudTable.add(new Image(barsBackground)).padTop(12f).padLeft(20f);
        
        
        barTable = new Table();
        barTable.top().left();
        barTable.setFillParent(true);
        barTable.padTop(15f).padLeft(27f);

        healthBarPadImage = new Image(barsPadRight);
        healthBarImage = new Image(healthBar);
        staminaBarPadImage = new Image(barsPadRight);
        staminaBarImage = new Image(staminaBar);
        magickaBarPadImage = new Image(barsPadRight);
        magickaBarImage = new Image(magickaBar);
        
        healthBarImage.setScaleX(barLength);
        staminaBarImage.setScaleX(barLength);
        magickaBarImage.setScaleX(barLength);

        barTable.add(healthBarImage);
        barTable.add(healthBarPadImage);
        barTable.row().padTop(2f);
        barTable.add(staminaBarImage);
        barTable.add(staminaBarPadImage);
        barTable.row().padTop(2f);
        //barTable.add(magickaBarImage);
        //barTable.add(magickaBarPadImage);


        extraTable = new Table();
        extraTable.top().right();
        extraTable.setFillParent(true);
        extraTable.padTop(20f).padRight(27f);

        extraItemImageOne = new Image(extraItem);
        extraItemImageTwo = new Image(extraItem);

        extraTable.add(extraItemImageOne);
        extraTable.row().padTop(5f);
        extraTable.add(extraItemImageTwo);


        textTable = new Table();
        textTable.top().left();
        textTable.setFillParent(true);
        //textTable.padTop(49f).padLeft(91f);
        textTable.padTop(23f).padLeft(49f);

        BitmapFont font = Font.REGULAR;
        weaponIconImage = new Image(weaponIcon);
        weaponNameLabel = new Label("Rustic Axe", new Label.LabelStyle(font, Color.WHITE));

        textTable.add(weaponIconImage).pad(5f);
        textTable.add(weaponNameLabel).padTop(18f);
        
        addActor(hudTable);
        addActor(barTable);
        addActor(extraTable);
        addActor(textTable);
    }


    public void registerPlayer(Player player) {
        playerStats = Mappers.STATS.get(player);
    }

    public void update(float delta) {
        int fullHealth = playerStats.getFullHealth();
        int fullStamina = playerStats.getFullStamina();
        int fullMagicka = playerStats.getFullMagicka();

        healthBarImage.setScaleX(barLength * playerStats.getHealth() / fullHealth); // 100 is only temporary (player's full heatlh is 100)
        staminaBarImage.setScaleX(barLength * playerStats.getStamina() / fullStamina);
        //healthBarPadImage.setX(healthBarImage.getX() + barLength * player.getHealth() / fullMagicka);
        healthBarPadImage.setX(healthBarImage.getX() + healthBarImage.getScaleX());
        staminaBarPadImage.setX(staminaBarImage.getX() + staminaBarImage.getScaleX());
        magickaBarPadImage.setX(magickaBarImage.getX() + magickaBarImage.getScaleX());
        //healthLabel.setText(String.format("HP: %d / %d", player.getHealth(), 100));
    }
    
}