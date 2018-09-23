package com.aesophor.medievania.event;

public enum GameEventType {

    DIALOG_OPTION,              // 0

    MAINGAME_SCREEN_RESIZED,    // 2
    GAME_PAUSED,                // 15
    GAME_RESUMED,               // 16

    ITEM_EQUIPPED,                 // 12
    ITEM_UNEQUIPPED,               // 13
    ITEM_PICKED_UP,             // 5

    MAP_CHANGED,                // 0
    PORTAL_USED,                // 1

    INFLICT_DAMAGE,             // 3
    CHARACTER_KILLED,           // 4



    INVENTORY_CHANGED,          // 8
    INVENTORY_TAB_SELECTED,     // 9
    INVENTORY_ITEM_SELECTED,    // 10
    ITEM_DISCARDED,             // 11

    PROMPT_DISCARD_ITEM,        // 14



}