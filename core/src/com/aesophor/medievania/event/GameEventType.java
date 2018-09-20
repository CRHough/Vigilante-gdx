package com.aesophor.medievania.event;

public enum GameEventType {

    MAP_CHANGED,                // 0
    PORTAL_USED,                // 1
    MAINGAME_SCREEN_RESIZED,    // 2
    HEALTH_CHANGED,             // 3
    CHARACTER_KILLED,           // 4
    ITEM_PICKED_UP,             // 5
    ITEM_EQUIPPED,              // 6
    ITEM_UNEQUIPPED,            // 7

    INVENTORY_CHANGED,          // 8
    INVENTORY_TAB_SELECTED,     // 9
    INVENTORY_ITEM_SELECTED,    // 10
    DISCARD_ITEM,               // 11
    EQUIP_ITEM,                 // 12
    PROMPT_DISCARD_ITEM,        // 13

}