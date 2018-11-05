package com.aesophor.vigilante.entity.data;

import com.aesophor.vigilante.component.character.CharacterDataComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import java.util.HashMap;
import java.util.Map;

public class CharacterDataManager {

    private static final Json JSON = new Json();
    private static final CharacterDataManager INSTANCE = new CharacterDataManager();

    private Map<String, CharacterDataComponent> enemyDataComponents;

    private CharacterDataManager() {

    }

    public static CharacterDataManager getInstance() {
        return INSTANCE;
    }


    /**
     * Loads a JSON file which contains all item data into enemyDataComponent HashMap.
     * @param filename JSON file.
     */
    public void load(String filename) {
        enemyDataComponents = JSON.fromJson(HashMap.class, CharacterDataComponent.class, Gdx.files.internal(filename).readString());
        Gdx.app.log("INFO", "Character data has finished loading" + filename);
    }

    /**
     * Gets CharacterDataComponent from the specified enemy name.
     * @param enemyName target item name.
     * @return data component of the enemy.
     */
    public CharacterDataComponent get(String enemyName) {
        return enemyDataComponents.get(enemyName);
    }

}