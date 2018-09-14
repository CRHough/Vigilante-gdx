package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public enum MenuItem {

    INVENTORY,      // 0
    EQUIPMENT,      // 1
    SKILLS,         // 2
    JOURNAL,        // 3
    OPTIONS;        // 4


    private static final Color HIGHLIGHT_ITEM_COLOR = Color.WHITE;
    private static final Color REGULAR_ITEM_COLOR = Color.GRAY;

    private static Array<Label> labels;
    private static int currentItemIdx;

    private Table table;

    public Texture getBackgroundTexture() {
        return ((MenuItemTable) table).getBackgroundTexture();
    }

    public Table getTable() {
        return table;
    }

    public void setTable(MenuItemTable table) {
        this.table = (Table) table;
    }

    @Override
    public String toString() {
        return name();
    }

    /**
     * Gets currently selected menu item.
     * @return currently selected menu item.
     */
    public static MenuItem current() {
        return MenuItem.values()[currentItemIdx];
    }

    /**
     * Forward to the next item in the menu. If it reaches the end, it will wrap around.
     */
    public static void next() {
        currentItemIdx++;
        if (currentItemIdx > MenuItem.values().length - 1) currentItemIdx = 0;
    }

    /**
     * Builds an array of labels from all available menu items.
     * @return an array of menu item labels.
     */
    public static Array<Label> buildLabels() {
        labels = new Array<>(MenuItem.values().length);
        for (MenuItem item : MenuItem.values()) {
            labels.add(new Label(item.toString(), LabelStyles.WHITE));
        }
        return labels;
    }

    /**
     * Updates menu item label colors. The currently selected item's label will be highlighted.
     */
    public static void updateLabelColors() {
        for (int i = 0; i < labels.size; i++) {
            if (i == currentItemIdx) labels.get(i).setColor(HIGHLIGHT_ITEM_COLOR);
            else labels.get(i).setColor(REGULAR_ITEM_COLOR);
        }
    }

    /**
     * Handles input for currently selected menu item.
     * @param delta
     */
    public void handleInput(float delta) {
        ((MenuItemTable) table).handleInput(delta);
    }

    /**
     * Shows the table of the specified target menu item. Other tables will be invisible.
     * @param target menu item to show.
     */
    public static void show(MenuItem target) {
        for (MenuItem item : MenuItem.values()) {
            if (item.getTable() != null) {
                item.getTable().setVisible(item == target);
            }
        }
    }

}