package com.aesophor.vigilante.entity.data;

import com.aesophor.vigilante.component.item.ItemDataComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import java.util.HashMap;
import java.util.Map;

public class ItemDataManager {

    private static final Json JSON = new Json();
    private static final ItemDataManager INSTANCE = new ItemDataManager();

    private Map<String, ItemDataComponent> itemDataComponents;

    private ItemDataManager() {

    }

    public static ItemDataManager getInstance() {
        return INSTANCE;
    }


    /**
     * Loads a JSON file which contains all item data into itemDataComponents HashMap.
     * @param filename JSON file.
     */
    public void load(String filename) {
        itemDataComponents = JSON.fromJson(HashMap.class, ItemDataComponent.class, Gdx.files.internal(filename).readString());
        Gdx.app.log("INFO", "Item data has finished loading" + filename);
    }

    /**
     * Gets ItemDataComponent from the specified item name.
     * @param itemName target item name.
     * @return item's data component.
     */
    public ItemDataComponent get(String itemName) {
        return itemDataComponents.get(itemName);
    }

}