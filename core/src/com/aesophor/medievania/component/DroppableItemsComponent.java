package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;
import java.util.Map;

public class DroppableItemsComponent implements Component {

    private final Map<String, Float> droppableItems;

    public DroppableItemsComponent() {
        droppableItems = new HashMap<>();
    }


    public void put(String itemName, float dropChance) {
        droppableItems.put(itemName, dropChance);
    }

    public Map<String, Float> get() {
        return droppableItems;
    }

}