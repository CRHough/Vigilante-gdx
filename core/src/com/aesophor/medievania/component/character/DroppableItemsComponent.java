package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;
import java.util.Map;

public class DroppableItemsComponent implements Component {

    private final Map<String, Float> droppableItems;

    public DroppableItemsComponent() {
        droppableItems = new HashMap<>();
    }

    public DroppableItemsComponent(HashMap<String, Float> droppableItems) {
        this();
        droppableItems.forEach(this.droppableItems::put);
    }


    public void put(String itemName, float dropRate) {
        droppableItems.put(itemName, dropRate);
    }

    public Map<String, Float> getDroppableItems() {
        return droppableItems;
    }

}