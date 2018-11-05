package com.aesophor.vigilante.component.character;

import com.aesophor.vigilante.component.item.ItemType;
import com.aesophor.vigilante.entity.item.Item;
import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.character.InventoryChangedEvent;
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


    public Array<Item> get(ItemType itemType) {
        return items.get(itemType);
    }

    public void add(Item item) {
        items.get(item.getType()).add(item);
        GameEventManager.getInstance().fireEvent(new InventoryChangedEvent());
    }

    public void remove(Item item) {
        items.get(item.getType()).removeValue(item, false);
        GameEventManager.getInstance().fireEvent(new InventoryChangedEvent());
    }

    @Override
    public String toString() {
        return items.toString();
    }

}