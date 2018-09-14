package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public enum MenuItem {

    INVENTORY,      // 0
    EQUIPMENT,      // 1
    SKILLS,         // 2
    JOURNAL,        // 3
    OPTIONS;        // 4


    private static Array<Label> labels;
    private static int currentItemIdx;

    @Override
    public String toString() {
        return name();
    }

    public static MenuItem current() {
        return MenuItem.values()[currentItemIdx];
    }

    public static void next() {
        currentItemIdx++;
        if (currentItemIdx > MenuItem.values().length - 1) currentItemIdx = 0;
    }

    public static Array<Label> buildLabels() {
        labels = new Array<>(MenuItem.values().length);
        for (MenuItem item : MenuItem.values()) {
            labels.add(new Label(item.toString(), LabelStyles.WHITE));
        }
        return labels;
    }

    public static void updateLabelColors() {
        for (int i = 0; i < labels.size; i++) {
            if (i == currentItemIdx) labels.get(i).setColor(Color.WHITE);
            else labels.get(i).setColor(Color.GRAY);
        }
    }

}