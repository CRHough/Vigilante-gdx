package com.aesophor.medievania.ui.component;

import com.aesophor.medievania.Asset;
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

    private class InventoryItem extends Stack {

        private Item item;
        private Image icon;
        private Label nameLabel;
        private Image selectionImage;

        public InventoryItem(Item item, Texture selectionTexture, Label.LabelStyle labelStyle) {
            this.item = item;
            this.icon = new Image((item != null) ? Mappers.SPRITE.get(item) : new Sprite(emptyItemTexture));
            this.nameLabel = new Label(" " + ((item != null) ? Mappers.ITEM_DATA.get(item).getName() : "----"), labelStyle);
            this.selectionImage = new Image(selectionTexture);

            HorizontalGroup group = new HorizontalGroup();
            group.padLeft(5f);
            group.addActor(icon);
            group.padRight(5f);
            group.addActor(nameLabel);

            this.add(this.selectionImage);
            this.add(group);
            this.setSelected(false);
        }


        public void setSelected(boolean selected) {
            selectionImage.setVisible(selected);
            if (selected) {
                GameEventManager.getInstance().fireEvent(new InventoryItemChangedEvent(item));
            }
        }

        public Item getItem() {
            return item;
        }
    }


    private final Texture selectionTexture;
    private final Texture emptyItemTexture;
    private final Sound clickSound;

    private final Table contentTable;
    private final Array<InventoryItem> items;

    private int currentItemIdx;
    private int navigateUpCounter;
    private int navigateDownCounter;

    public ItemListView(AssetManager assets) {
        super(new Table());
        selectionTexture = assets.get(Asset.ITEM_HIGHLIGHTED);
        emptyItemTexture = assets.get(Asset.EMPTY_ITEM);
        clickSound = assets.get(Asset.UI_CLICK_SOUND, Sound.class);

        setScrollingDisabled(true, false);
        setOverscroll(false, false);

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

        for (InventoryItem i : items) {
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
        InventoryItem i = new InventoryItem(item, selectionTexture, LabelStyles.WHITE_REGULAR);
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
        return (currentItemIdx >= 0) ? items.get(currentItemIdx).getItem() : null;
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

                clickSound.play();
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

                clickSound.play();
            }
        }
    }

}