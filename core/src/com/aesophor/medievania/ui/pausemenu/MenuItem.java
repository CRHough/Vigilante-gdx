package com.aesophor.medievania.ui.pausemenu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public enum MenuItem {

    INVENTORY("Inventory"),   // 0
    EQUIPMENT("Equipment"),   // 1
    SKILLS("Skills"),         // 2
    JOURNAL("Journal"),       // 3
    OPTIONS("Options");       // 4


    private final String s;

    private MenuItem(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }

}