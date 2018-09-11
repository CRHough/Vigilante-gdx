package com.aesophor.medievania.component;

import com.aesophor.medievania.entity.item.Item;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class PickupItemTargetComponent implements Component {

    private final Array<Item> inRangeItems;

    public PickupItemTargetComponent() {
        inRangeItems = new Array<>();
    }


    public boolean hasInRangeItem() {
        return inRangeItems.size > 0;
    }

    public Array<Item> getInRangeItems() {
        return inRangeItems;
    }

    public void addInRangeItems(Item item) {
        this.inRangeItems.add(item);
    }

    public void removeInRangeItems(Item item) {
        this.inRangeItems.removeValue(item, false);
    }

}