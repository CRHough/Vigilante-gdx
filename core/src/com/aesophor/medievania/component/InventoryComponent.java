package com.aesophor.medievania.component;

import com.aesophor.medievania.entity.item.Item;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.Map;

public class InventoryComponent implements Component {

    private final Map<ItemType, Array<Item>> items;


    public InventoryComponent() {
        items = new HashMap<>();

        for (ItemType itemType : ItemType.values()) {
            items.put(itemType, new Array<>());
        }
    }

    // I should fire an inventoryChangedEvent in this class

    public Map<ItemType, Array<Item>> getItems() {
        return items;
    }

    public Array<Item> get(ItemType itemType) {
        return items.get(itemType);
    }

    @Override
    public String toString() {
        return items.toString();
    }

}