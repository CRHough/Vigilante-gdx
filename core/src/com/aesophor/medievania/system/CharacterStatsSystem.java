package com.aesophor.medievania.system;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.CharacterStatsComponent;
import com.aesophor.medievania.component.equipment.EquipmentDataComponent;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.character.ItemEquippedEvent;
import com.aesophor.medievania.event.character.ItemUnequippedEvent;
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