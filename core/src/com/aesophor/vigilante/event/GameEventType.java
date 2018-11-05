package com.aesophor.vigilante.event;

public enum GameEventType {

    // Screen related events.
    MAINGAME_SCREEN_RESIZED,
    GAME_PAUSED,
    GAME_RESUMED,

    // UI related events.
    MENU_DIALOG_OPTION,
    INVENTORY_TAB_SELECTED,
    INVENTORY_ITEM_SELECTED,

    DIALOG_STARTED,
    DIALOG_ENDED,

    // Inventory related events.
    INVENTORY_CHANGED,
    ITEM_EQUIPPED,
    ITEM_UNEQUIPPED,
    ITEM_PICKED_UP,
    ITEM_DISCARDED,

    // Map related events.
    PORTAL_USED,
    MAP_CHANGED,

    // Combat related events.
    INFLICT_DAMAGE,
    CHARACTER_KILLED;

}