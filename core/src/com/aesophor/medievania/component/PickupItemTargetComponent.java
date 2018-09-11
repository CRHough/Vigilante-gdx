package com.aesophor.medievania.component;

import com.aesophor.medievania.entity.item.Item;
import com.badlogic.ashley.core.Component;

public class PickupItemTargetComponent implements Component {

    private Item inRangeItem;

    public boolean hasInRangeItem() {
        return inRangeItem != null;
    }

    public Item getInRangeItem() {
        return inRangeItem;
    }

    public void setInRangeItem(Item inRangeItem) {
        this.inRangeItem = inRangeItem;
    }

}