package com.aesophor.medievania.entity.data;

import com.aesophor.medievania.component.equipment.EquipmentDataComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import java.util.HashMap;
import java.util.Map;

public class EquipmentDataManager {

    private static final Json JSON = new Json();
    private static final EquipmentDataManager INSTANCE = new EquipmentDataManager();

    private Map<String, EquipmentDataComponent> equipmentDataComponents;

    private EquipmentDataManager() {

    }

    public static EquipmentDataManager getInstance() {
        return INSTANCE;
    }


    /**
     * Loads a JSON file which contains all item data into itemDataComponents HashMap.
     * @param filename JSON file.
     */
    public void load(String filename) {
        equipmentDataComponents = JSON.fromJson(HashMap.class, EquipmentDataComponent.class, Gdx.files.internal(filename).readString());
        Gdx.app.log("INFO", "Equipment data has finished loading" + filename);
    }

    /**
     * Gets ItemDataComponent from the specified item name.
     * @param itemName target item name.
     * @return item's data component.
     */
    public EquipmentDataComponent get(String itemName) {
        return equipmentDataComponents.get(itemName);
    }

}