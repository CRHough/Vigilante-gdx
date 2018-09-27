package com.aesophor.medievania.ui.component;

import com.aesophor.medievania.GameAssetManager;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.InventoryComponent;
import com.aesophor.medievania.component.equipment.EquipmentDataComponent;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.ui.InventoryItemChangedEvent;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;

public class ItemListView extends ScrollPane {

    private class ListViewItem extends Stack {

        private static final String EMPTY_ITEM_TEXT = "----"; // The name to display on empty item's label.
        private static final float PADDING = 5f; // Left / right padding.
        private static final float SPACING = 5f; // Space between each of its content (icon and label).
        private final Image highlightedItemImage;

        private final Item item;
        private final Image icon;
        private final Label name;

        public ListViewItem(Item item, Label.LabelStyle labelStyle) {
            this.highlightedItemImage = new Image(highlightedItemTexture);

            this.item = item;
            this.icon = new Image((item != null) ? Mappers.SPRITE.get(item) : new Sprite(emptyItemIconTexture));
            this.name = new Label(((item != null) ? Mappers.ITEM_DATA.get(item).getName() : EMPTY_ITEM_TEXT), labelStyle);

            HorizontalGroup group = new HorizontalGroup();
            group.padLeft(PADDING);
            group.padRight(PADDING);
            group.space(SPACING);

            group.addActor(icon);
            group.addActor(name);

            this.add(highlightedItemImage);
            this.add(group);
            this.setSelected(false);
        }


        public void setSelected(boolean isSelected) {
            highlightedItemImage.setVisible(isSelected);

            if (isSelected) {
                GameEventManager.getInstance().fireEvent(new InventoryItemChangedEvent(item));
                clickSound.play();
            }
        }

        public Item getItem() {
            return item;
        }
    }


    private final Texture highlightedItemTexture;
    private final Texture emptyItemIconTexture;
    private final Sound clickSound;

    private final Table contentTable;
    private final Array<ListViewItem> items;

    private int currentItemIdx;
    private int navigateUpCounter;
    private int navigateDownCounter;

    public ItemListView(AssetManager assets, float width, float height) {
        super(new Table());
        setSize(width, height);

        // Initialize assets.
        highlightedItemTexture = assets.get(GameAssetManager.ITEM_HIGHLIGHTED);
        emptyItemIconTexture = assets.get(GameAssetManager.EMPTY_ITEM);
        clickSound = assets.get(GameAssetManager.UI_CLICK_SOUND, Sound.class);

        // Disable overscroll effects and X coordinate scrolling.
        setOverscroll(false, false);
        setScrollingDisabled(true, false);

        contentTable = (Table) this.getActor();
        contentTable.top().left();
        contentTable.defaults().spaceTop(5f).left();

        items = new Array<>();
        currentItemIdx = -1;
    }


    /**
     * Inserts an empty item.
     * @return this ItemListView instance.
     */
    public ItemListView insertEmptyItem() {
        add(null);
        return this;
    }

    /**
     * Populates the list view with the specified type of items from the specified inventory.
     * @param inventory target inventory to display.
     * @param type type of items to display.
     * @return this ItemListView instance.
     */
    public ItemListView populate(InventoryComponent inventory, ItemType type) {
        inventory.get(type).forEach(this::add);

        if (items.size > 0) {
            currentItemIdx = 0;
            items.first().setSelected(true);
            GameEventManager.getInstance().fireEvent(new InventoryItemChangedEvent(items.get(currentItemIdx).getItem()));
        }
        return this;
    }

    /**
     * Filters the list view with the specified type of equipment.
     * @param type type of equipment to display.
     * @return this ItemListView instance.
     */
    public ItemListView filter(EquipmentType type) {
        contentTable.clearChildren();

        for (ListViewItem i : items) {
            if (i.getItem() == null) {
                contentTable.add(i).row();
                continue;
            }
            EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(i.getItem());
            if (equipmentData.getType() == type) {
                contentTable.add(i).row();
            }
        }
        return this;
    }

    /**
     * Clears the list view.
     */
    public void clear() {
        contentTable.clearChildren();
        items.clear();
    }

    /**
     * Adds the specified item to the list view.
     * @param item item to add.
     */
    public void add(Item item) {
        ListViewItem i = new ListViewItem(item, LabelStyles.WHITE_REGULAR);
        items.add(i);
        contentTable.add(i).row();

        currentItemIdx = 0;
        items.first().setSelected(true);
    }

    /**
     * Gets the currently selected item.
     * @return selected item.
     */
    public Item getSelectedItem() {
        return (items.size > 0) ? items.get(currentItemIdx).getItem() : null;
    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (currentItemIdx > 0) {
                // Highlight the previous itemData.
                items.get(currentItemIdx).setSelected(false);
                currentItemIdx--;
                items.get(currentItemIdx).setSelected(true);

                // Decrement the navigateDownCounter so that if user suddenly navigate downward,
                // the scrollpane can scroll down correctly later.
                if (navigateDownCounter > 0) {
                    navigateDownCounter--;
                }

                navigateUpCounter++;
                if (navigateUpCounter >= 5) {
                    setScrollY(getScrollY() - 24f);
                    navigateUpCounter--;
                }
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (currentItemIdx < items.size - 1) {
                // Highlight the next itemData.
                items.get(currentItemIdx).setSelected(false);
                currentItemIdx++;
                items.get(currentItemIdx).setSelected(true);

                // Increment the navigateUpCounter so that if user suddenly navigate upward,
                // the scrollpane can scroll up correctly later.
                if (navigateUpCounter > 0) {
                    navigateUpCounter--;
                }

                navigateDownCounter++;

                if (navigateDownCounter >= 5) {
                    setScrollY(getScrollY() + 24f);
                    navigateDownCounter--;
                }
            }
        }
    }

}