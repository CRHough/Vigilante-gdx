package com.aesophor.medievania;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.HashMap;
import java.util.Map;

public class Asset extends AssetManager {

    public static final String CHARACTER_DATABASE = "Database/characters.json";
    public static final String ITEM_DATABASE = "Database/items.json";
    public static final String EQUIPMENT_DATABASE = "Database/equipment.json";

    // UI Textures
    public static final String HEADER_FONT = "Font/MatchupPro.ttf";
    public static final String REGULAR_FONT = "Font/HeartbitXX.ttf";

    public static final String MAIN_MENU_BG = "Texture/UI/mainmenu_bg.png";
    public static final String PAUSE_MENU_BG = "Texture/UI/pause.png";
    public static final String MESSAGE_BOX_BG = "Texture/UI/messagebox.png";
    public static final String STATS_BG = "Texture/UI/stats_bg.png";
    public static final String INVENTORY_BG = "Texture/UI/inventory_bg.png";
    public static final String TRIANGLE = "Texture/UI/triangle.png";

    public static final String HUD_TEXTURE = "Texture/UI/status_bar.png";

    public static final String TAB_REGULAR = "Texture/UI/tab_regular.png";
    public static final String TAB_HIGHLIGHTED = "Texture/UI/tab_highlighted.png";

    // Item
    public static final String ITEM_REGULAR = "Texture/UI/item_regular.png";
    public static final String ITEM_HIGHLIGHTED = "Texture/UI/item_highlighted.png";
    public static final String EQUIPMENT_REGULAR = "Texture/UI/equipment_regular.png";
    public static final String EQUIPMENT_HIGHLIGHTED = "Texture/UI/equipment_highlighted.png";

    public static final String UI_CLICK_SOUND = "Sfx/UI/click.wav";
    public static final String OPEN_CLOSE_SOUND = "Sfx/Inventory/open_and_close.wav";
    public static final String EQUIP_SOUND = "Sfx/Player/equip.wav";
    public static final String HURT_SOUND = "Sfx/Player/hurt.wav";
    public static final String KILLED_SOUND = "Sfx/Player/death.mp3";
    public static final String WEAPON_SWING_SOUND = "Sfx/Player/weapon_swing.ogg";
    public static final String WEAPON_HIT_SOUND = "Sfx/Player/weapon_hit.ogg";
    public static final String ITEM_PICKEDUP_SOUND = "Sfx/Player/pickup_item.mp3";
    public static final String JUMP_SOUND = "Sfx/Player/jump.wav";


    public static final String MAIN_MENU_MUSIC = "Music/main_menu.wav";
    public static final String WATER_DRIPPING = "Sfx/Environment/water_dripping.mp3";
    public static final String VILLAGE_MUSIC = "Music/village01.mp3";

    public static final String BANDIT_ATLAS = "Texture/Character/bandit/Bandit.pack";
    public static final String KNIGHT_ATLAS = "Texture/Character/knight/Knight.pack";

    public static final String TEXTURE_DUST = "Texture/FX/dust.png";
    public static final String TEXTURE_BAT = "Texture/Skill/bat.png";
    public static final String EMPTY_ITEM = "Texture/Item/empty.png";
    public static final String TEXTURE_AXE = "Texture/Item/rusty_axe.png";


    private final Map<String, Class<?>> assets;

    public Asset() {
        assets = new HashMap<>();

        register(Asset.MAIN_MENU_BG, Texture.class);
        register(Asset.HUD_TEXTURE, Texture.class);
        register(Asset.MESSAGE_BOX_BG, Texture.class);
        register(Asset.PAUSE_MENU_BG, Texture.class);
        register(Asset.STATS_BG, Texture.class);
        register(Asset.INVENTORY_BG, Texture.class);
        register(Asset.TAB_REGULAR, Texture.class);
        register(Asset.TAB_HIGHLIGHTED, Texture.class);
        register(Asset.ITEM_REGULAR, Texture.class);
        register(Asset.ITEM_HIGHLIGHTED, Texture.class);
        register(Asset.EQUIPMENT_REGULAR, Texture.class);
        register(Asset.EQUIPMENT_HIGHLIGHTED, Texture.class);
        register(Asset.TRIANGLE, Texture.class);

        register(Asset.BANDIT_ATLAS, TextureAtlas.class);
        register(Asset.KNIGHT_ATLAS, TextureAtlas.class);
        register(Asset.TEXTURE_DUST, Texture.class);
        register(Asset.TEXTURE_BAT, Texture.class);

        register(Asset.EMPTY_ITEM, Texture.class);
        register(Asset.TEXTURE_AXE, Texture.class);
        register(Asset.MAIN_MENU_MUSIC, Music.class);
        register(Asset.WATER_DRIPPING, Music.class);
        register(Asset.VILLAGE_MUSIC, Music.class);
        register(Asset.OPEN_CLOSE_SOUND, Sound.class);
        register(Asset.EQUIP_SOUND, Sound.class);
        register(Asset.HURT_SOUND, Sound.class);
        register(Asset.KILLED_SOUND, Sound.class);
        register(Asset.WEAPON_SWING_SOUND, Sound.class);
        register(Asset.WEAPON_HIT_SOUND, Sound.class);
        register(Asset.ITEM_PICKEDUP_SOUND, Sound.class);
        register(Asset.JUMP_SOUND, Sound.class);
        register(Asset.UI_CLICK_SOUND, Sound.class);
    }


    public void loadAllAssets() {
        assets.forEach(this::load);
    }

    public void register(String fileName, Class<?> assetType) {
        assets.put(fileName, assetType);
    }

    /**
     * Loads and gets the specified asset.
     * @param fileName file name of the asset to load.
     * @param <T> class type of the asset.
     * @return loaded asset.
     */
    @Override
    public <T> T get(String fileName) {
        load(fileName, assets.get(fileName));
        System.out.println(fileName + ": " + getReferenceCount(fileName));
        return super.get(fileName);
    }

}