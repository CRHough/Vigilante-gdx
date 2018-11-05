package com.aesophor.vigilante.event.character;

import com.aesophor.vigilante.entity.character.Character;
import com.aesophor.vigilante.entity.item.Item;
import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class ItemUnequippedEvent extends GameEvent {

    private final Character character;
    private final Item item;

    public ItemUnequippedEvent(Character character, Item item) {
        super(GameEventType.ITEM_UNEQUIPPED);
        this.character = character;
        this.item = item;
    }


    public Character getCharacter() {
        return character;
    }

    public Item getItem() {
        return item;
    }

}