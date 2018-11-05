package com.aesophor.vigilante.system;

import com.aesophor.vigilante.component.Mappers;
import com.aesophor.vigilante.component.character.CharacterStatsComponent;
import com.aesophor.vigilante.component.equipment.EquipmentDataComponent;
import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.event.character.ItemEquippedEvent;
import com.aesophor.vigilante.event.character.ItemUnequippedEvent;
import com.badlogic.ashley.core.EntitySystem;

public class CharacterStatsSystem extends EntitySystem {

    public CharacterStatsSystem() {
        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_EQUIPPED, (ItemEquippedEvent e) -> {
            CharacterStatsComponent stats = Mappers.STATS.get(e.getCharacter());
            EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());

            stats.setBasePhysicalDamage(stats.getBasePhysicalDamage() + equipmentData.getBonusPhysicalDamage());
        });

        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_UNEQUIPPED, (ItemUnequippedEvent e) -> {
            CharacterStatsComponent stats = Mappers.STATS.get(e.getCharacter());
            EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());

            stats.setBasePhysicalDamage(stats.getBasePhysicalDamage() - equipmentData.getBonusPhysicalDamage());
        });
    }

}