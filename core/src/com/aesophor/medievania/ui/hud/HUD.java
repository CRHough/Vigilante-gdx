package com.aesophor.medievania.ui.hud;

import com.aesophor.medievania.GameAssetManager;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.StatsComponent;
import com.aesophor.medievania.component.equipment.EquipmentDataComponent;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.component.item.ItemDataComponent;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.character.ItemEquippedEvent;
import com.aesophor.medievania.event.character.ItemUnequippedEvent;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class HUD extends Stage {

    private static int barLength = 75; // pixel

    private StatsComponent playerStats;
    private int lastUpdatedHealth;
    private int lastUpdatedStamina;
    private int lastUpdatedMagicka;

    private final StatusBar healthBar;
    private final StatusBar staminaBar;
    private final StatusBar magickaBar;

    private final Slot equippedWeaponSlot;

    /*
    private Image healthBarImage;
    private Image healthBarPadImage;
    private Image staminaBarImage;
    private Image staminaBarPadImage;
    private Image magickaBarImage;
    private Image magickaBarPadImage;


    private Texture weaponIconTexture;
    private Image weaponIconImage;
    private Label weaponNameLabel;
    
    private Table hudTable;
    private Table barTable;
    private Table extraTable;
    private Table textTable;
    */

    public HUD(AssetManager assets, Batch batch) {
        this(assets, batch, null);
    }
    
    public HUD(AssetManager assets, Batch batch, Player player) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), batch);

        // Extract regions from the texture atlas.
        TextureAtlas atlas = assets.get(GameAssetManager.HUD_ATLAS);
        TextureAtlas.AtlasRegion barPadLeftRegion = atlas.findRegion("bar_padding_left");
        TextureAtlas.AtlasRegion barPadRightRegion = atlas.findRegion("bar_padding_right");
        TextureAtlas.AtlasRegion healthBarRegion = atlas.findRegion("health_bar");
        TextureAtlas.AtlasRegion staminaBarRegion = atlas.findRegion("stamina_bar");
        TextureAtlas.AtlasRegion magickaBarRegion = atlas.findRegion("magicka_bar");
        TextureAtlas.AtlasRegion equippedWeaponSlotRegion = atlas.findRegion("equipped_weapon_slot");
        TextureAtlas.AtlasRegion itemDescRegion = atlas.findRegion("item_desc");

        if (player != null) {
            playerStats = Mappers.STATS.get(player);
        }
        
        // Initializes player hud Texture and TextureRegions.
        

        /*
        hudTable = new Table();
        hudTable.top().left();
        hudTable.setFillParent(true);
        //hudTable.add(new Image(barsBackground)).padTop(12f).padLeft(20f);
        */
        
        Table barTable = new Table();
        barTable.top().left();
        barTable.setFillParent(true);
        barTable.padTop(15f).padLeft(27f);
        barTable.defaults().padTop(2f);

        healthBar = new StatusBar(barPadLeftRegion, barPadRightRegion, healthBarRegion, 75f);
        staminaBar = new StatusBar(barPadLeftRegion, barPadRightRegion, staminaBarRegion, 75f);
        magickaBar = new StatusBar(barPadLeftRegion, barPadRightRegion, magickaBarRegion, 75f);

        barTable.add(healthBar).row();
        barTable.add(staminaBar).row();
        barTable.add(magickaBar).row();



        equippedWeaponSlot = new Slot(equippedWeaponSlotRegion, itemDescRegion, 28f, -45f);

        /*
        extraTable = new Table();
        extraTable.top().right();
        extraTable.setFillParent(true);
        extraTable.padTop(20f).padRight(27f);
        */

        /*
        extraItemImageOne = new Image(extraItem);
        extraItemImageTwo = new Image(extraItem);

        extraTable.add(extraItemImageOne);
        extraTable.row().padTop(5f);
        extraTable.add(extraItemImageTwo);
        */

        /*
        textTable = new Table();
        textTable.top().left();
        textTable.setFillParent(true);
        //textTable.padTop(49f).padLeft(91f);
        textTable.padTop(23f).padLeft(49f);

        BitmapFont font = Font.REGULAR;
        weaponIconImage = new Image();
        weaponIconImage.setScale(1.5f);
        weaponNameLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        textTable.add(weaponIconImage).padTop(11f).padLeft(9f).padRight(17f);
        textTable.add(weaponNameLabel).padTop(25f);
        */
        
        //addActor(hudTable);
        addActor(barTable);
        addActor(equippedWeaponSlot);
        //addActor(extraTable);
        //addActor(textTable);


        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_EQUIPPED, (ItemEquippedEvent e) -> {
            if (e.getCharacter().equals(player) || e.getItem().getType() == ItemType.EQUIP) {
                EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());

                if (equipmentData.getType() == EquipmentType.WEAPON) {
                    e.getItem().reloadTexture();
                    equippedWeaponSlot.update(e.getItem());
                }
            }
        });

        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_UNEQUIPPED, (ItemUnequippedEvent e) -> {
            if (e.getCharacter().equals(player) || e.getItem().getType() == ItemType.EQUIP) {
                EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());

                if (equipmentData.getType() == EquipmentType.WEAPON) {
                    assets.unload(Mappers.ITEM_DATA.get(e.getItem()).getImage());
                    equippedWeaponSlot.update(null);
                }
            }
        });

    }


    public void registerPlayer(Player player) {
        playerStats = Mappers.STATS.get(player);
    }

    public void update(float delta) {
        healthBar.update(playerStats.getHealth(), playerStats.getFullHealth());
        staminaBar.update(playerStats.getStamina(), playerStats.getFullStamina());
        magickaBar.update(playerStats.getMagicka(), playerStats.getFullMagicka());
    }
    
}