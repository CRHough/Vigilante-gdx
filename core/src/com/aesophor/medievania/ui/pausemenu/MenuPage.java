package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.ui.theme.Colorscheme;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

/**
 * Menu page is an enum that contains all "pages" in the Pause Menu.
 * Each page can contain multiple "panes",
 *
 * Each pane can contain a background texture (the background texture can be null).
 * If it's not null, it will be rendered depending on the pane's position.
 *
 * Each pane must implement the abstract method handleInput() which handles
 * the input to the pane.
 */
public enum MenuPage {

    INVENTORY,      // 0
    EQUIPMENT,      // 1
    SKILLS,         // 2
    JOURNAL,        // 3
    OPTIONS;        // 4


    private static final Color HIGHLIGHT_ITEM_COLOR = Color.WHITE;
    private static final Color REGULAR_ITEM_COLOR = Colorscheme.GREY;

    private static Array<MenuPage> renderQueue;
    private static Array<Label> labels;
    private static int currentItemIdx;

    private final Array<Pane> panes;

    static {
        renderQueue = new Array<>();
        labels = new Array<>(MenuPage.values().length);
    }

    private MenuPage() {
        panes = new Array<>();
    }


    /**
     * Gets all panes within this menu page.
     * @return
     */
    public Array<Pane> getPanes() {
        return panes;
    }

    /**
     * Adds the specified pane to this menu page.
     * @param pane
     */
    public void addPane(Pane pane) {
        panes.add(pane);
    }

    @Override
    public String toString() {
        return name();
    }

    /**
     * Handles input for currently selected menu page.
     * @param delta delta time.
     */
    public void handleInput(float delta) {
        // If any page has tried to show another page by calling show(),
        // that new page to be rendered will be stored in renderQueue.
        // Directly rendering the new page while the line below hasn't finished
        // will cause problem.
        panes.forEach(pane -> pane.handleInput(delta));

        // Should there be any of them, it is now safe to render it.
        MenuPage.renderPageInQueue();
    }


    /**
     * Gets currently selected menu page.
     * @return currently selected menu page.
     */
    public static MenuPage current() {
        return MenuPage.values()[currentItemIdx];
    }

    /**
     * Go back to the previous page in the menu. If it reaches the beginning, it will wrap around.
     */
    public static void prev() {
        currentItemIdx--;
        if (currentItemIdx < 0) currentItemIdx = MenuPage.values().length - 1;
    }

    /**
     * Forward to the next page in the menu. If it reaches the end, it will wrap around.
     */
    public static void next() {
        currentItemIdx++;
        if (currentItemIdx > MenuPage.values().length - 1) currentItemIdx = 0;
    }

    /**
     * Shows the specified menu page. The actual rendering of the target page will be postponed
     * until handleInput() on all panes of current page has finished. (Otherwise it would
     * throw an exception)
     * @param target menu page to show.
     */
    public static void show(MenuPage target) {
        renderQueue.add(target);
    }

    private static void renderPageInQueue() {
        if (renderQueue.size > 0) {
            currentItemIdx = renderQueue.first().ordinal();
            renderQueue.removeIndex(0);
            MenuPage.update(MenuPage.current());
        }
    }


    /**
     * Builds an array of labels from all available menu pages.
     * @return an array of menu page labels.
     */
    public static Array<Label> buildLabels() {
        labels.clear();
        for (MenuPage item : MenuPage.values()) {
            labels.add(new Label(item.toString(), LabelStyles.WHITE_HEADER));
        }
        return labels;
    }

    /**
     * Updates menu page label colors. The currently selected page's label will be highlighted.
     */
    public static void updateLabelColors() {
        for (int i = 0; i < labels.size; i++) {
            if (i == currentItemIdx) labels.get(i).setColor(HIGHLIGHT_ITEM_COLOR);
            else labels.get(i).setColor(REGULAR_ITEM_COLOR);
        }
    }

    /**
     * Updates all panes of the specified target menu page. Tables that belong to any menu page
     * other than the currently selected one will be invisible.
     * @param target menu page to update.
     */
    public static void update(MenuPage target) {
        for (MenuPage item : MenuPage.values()) {
            item.getPanes().forEach(table -> table.setVisible(item == target));
        }
    }

}