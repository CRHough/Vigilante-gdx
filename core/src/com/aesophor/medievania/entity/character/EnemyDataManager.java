package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.EnemyDataComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import java.util.HashMap;
import java.util.Map;

public class EnemyDataManager {

    private static final Json JSON = new Json();
    private static final EnemyDataManager INSTANCE = new EnemyDataManager();

    private Map<String, EnemyDataComponent> enemyDataComponents;

    private EnemyDataManager() {

    }

    public static EnemyDataManager getInstance() {
        return INSTANCE;
    }


    /**
     * Loads a JSON file which contains all item data into enemyDataComponent HashMap.
     * @param filename JSON file.
     */
    public void load(String filename) {
        enemyDataComponents = JSON.fromJson(HashMap.class, EnemyDataComponent.class, Gdx.files.internal(filename).readString());
        Gdx.app.log("EnemyDataManager", "finished loading" + filename);
    }

    /**
     * Gets EnemyDataComponent from the specified enemy name.
     * @param enemyName target item name.
     * @return data component of the enemy.
     */
    public EnemyDataComponent get(String enemyName) {
        return enemyDataComponents.get(enemyName);
    }

}