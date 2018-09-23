package com.aesophor.medievania.event;

public enum GameEventType {

    // Screen related events.
    MAINGAME_SCREEN_RESIZED,    // 0
    GAME_PAUSED,                // 1
    GAME_RESUMED,               // 2

    // UI related events.
    DIALOG_OPTION,              // 3
    INVENTORY_TAB_SELECTED,     // 4
    INVENTORY_ITEM_SELECTED,    // 5

    // Inventory related events.
    INVENTORY_CHANGED,          // 6
    ITEM_EQUIPPED,              // 7
    ITEM_UNEQUIPPED,            // 8
    ITEM_PICKED_UP,             // 9
    ITEM_DISCARDED,             // 10

    // Map related events.
    PORTAL_USED,                // 11
    MAP_CHANGED,                // 12

    // Combat related events.
    INFLICT_DAMAGE,             // 13
    CHARACTER_KILLED,           // 14

}