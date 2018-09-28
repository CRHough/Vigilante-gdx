package com.aesophor.medievania.ui.hud;

import com.aesophor.medievania.GameAssetManager;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.StatsComponent;
import com.aesophor.medievania.component.equipment.EquipmentDataComponent;
import com.aesophor.medievania.component.equipment.EquipmentType;
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

    private Player player;
    private final Bar healthBar;
    private final Bar staminaBar;
    private final Bar magickaBar;
    private final Slot equippedWeaponSlot;

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


        // Initialize health, stamina and magicka bars.
        healthBar = new Bar(barPadLeftRegion, barPadRightRegion, healthBarRegion, 75f);
        staminaBar = new Bar(barPadLeftRegion, barPadRightRegion, staminaBarRegion, 75f);
        magickaBar = new Bar(barPadLeftRegion, barPadRightRegion, magickaBarRegion, 75f);

        Table barTable = new Table();
        barTable.top().left();
        barTable.setFillParent(true);
        barTable.setPosition(62f, -20f);

        barTable.defaults().padTop(2f);
        barTable.add(healthBar).row();
        barTable.add(staminaBar).row();
        barTable.add(magickaBar).row();


        // Initialize equipped weapon slot. (Note that the slot itself is a table).
        equippedWeaponSlot = new Slot(equippedWeaponSlotRegion, itemDescRegion, 28f, -20f);

        addActor(barTable);
        addActor(equippedWeaponSlot);


        // When player equip a weapon, display the currently equipped weapon on equipped weapon slot.
        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_EQUIPPED, (ItemEquippedEvent e) -> {
            if (e.getCharacter().equals(player) || e.getItem().getType() == ItemType.EQUIP) {
                EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());

                if (equipmentData.getType() == EquipmentType.WEAPON) {
                    e.getItem().reloadTexture();
                    equippedWeaponSlot.update(e.getItem());
                }
            }
        });

        // When player unequips its weapon, remove the weapon from equipped weapon slot.
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
        this.player = player;
    }

    public void update(float delta) {
        StatsComponent playerStats = Mappers.STATS.get(player);
        healthBar.update(playerStats.getHealth(), playerStats.getFullHealth());
        staminaBar.update(playerStats.getStamina(), playerStats.getFullStamina());
        magickaBar.update(playerStats.getMagicka(), playerStats.getFullMagicka());
    }
    
}