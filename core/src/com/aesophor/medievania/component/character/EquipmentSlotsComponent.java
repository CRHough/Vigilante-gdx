package com.aesophor.medievania.component.character;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.entity.item.Item;
import com.badlogic.ashley.core.Component;
import java.util.HashMap;
import java.util.Map;

public class EquipmentSlotsComponent implements Component {

    private final Map<EquipmentType, Item> equipment;

    public EquipmentSlotsComponent() {
        equipment = new HashMap<>();

        for (EquipmentType type : EquipmentType.values()) {
            equipment.put(type, null);
        }
    }


    public Item get(EquipmentType type) {
        return equipment.get(type);
    }

    public void put(Item item) {
        equipment.put(Mappers.EQUIPMENT_DATA.get(item).getType(), item);
    }

    public void remove(Item item) {
        equipment.put(Mappers.EQUIPMENT_DATA.get(item).getType(), null);
    }

    public boolean has(EquipmentType type) {
        return equipment.get(type) != null;
    }

    @Override
    public String toString() {
        return equipment.toString();
    }

}