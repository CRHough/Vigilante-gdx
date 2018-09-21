package com.aesophor.medievania.ui;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.InventoryComponent;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.ui.InventoryItemChangedEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;

public class ItemListView {

    private class InventoryItem extends Stack {

        private Item item;
        private Label nameLabel;
        private Image selectionImage;

        public InventoryItem(Item item, Texture selectionTexture, Label.LabelStyle labelStyle) {
            this.item = item;
            this.nameLabel = new Label(" " + Mappers.ITEM_DATA.get(item).getName(), labelStyle);
            this.selectionImage = new Image(selectionTexture);

            this.add(this.selectionImage);
            this.add(nameLabel);
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
    private final Sound clickSound;

    private final ScrollPane scrollPane;
    private final Table contentTable;

    private final Array<InventoryItem> items;
    private int currentItemIdx;
    private int navigateUpCounter;
    private int navigateDownCounter;

    public ItemListView(AssetManager assets, Player player) {
        selectionTexture = assets.get("interface/selection.png");
        clickSound = assets.get("sfx/ui/click.wav", Sound.class);

        contentTable = new Table();
        contentTable.top().left();
        contentTable.defaults().spaceTop(5f).left();

        scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);

        items = new Array<>();
        currentItemIdx = -1;
    }


    /**
     * Populates the list view with the specified type of items from player inventory.
     * @param type type of items to display.
     * @param player player entity which will be used for retrieving its inventory component.
     */
    public void populate(ItemType type, Player player) {
        InventoryComponent inventory = Mappers.INVENTORY.get(player);

        inventory.get(type).forEach(item -> {
            InventoryItem i = new InventoryItem(item, selectionTexture, LabelStyles.WHITE_REGULAR);
            items.add(i);
            contentTable.add(i).row();
        });

        if (items.size > 0) {
            currentItemIdx = 0;
            items.first().setSelected(true);
            GameEventManager.getInstance().fireEvent(new InventoryItemChangedEvent(items.get(currentItemIdx).getItem()));
        }
    }

    /**
     * Adds the specified item to the list view.
     * @param item item to add.
     */
    public void add(Item item) {
        InventoryItem i = new InventoryItem(item, selectionTexture, LabelStyles.WHITE_REGULAR);

        items.add(i);
        contentTable.add(i).row();

        if (getSelectedItem() == null) {
            currentItemIdx = 0;
            items.first().setSelected(true);
        }
    }

    /**
     * Clears the list view.
     */
    public void clear() {
        contentTable.clearChildren();
        items.clear();
    }

    /**
     * Gets the currently selected item.
     * @return selected item.
     */
    public Item getSelectedItem() {
        return (currentItemIdx >= 0) ? items.get(currentItemIdx).getItem() : null;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
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
                    scrollPane.setScrollY(scrollPane.getScrollY() - 24f);
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
                    scrollPane.setScrollY(scrollPane.getScrollY() + 24f);
                    navigateDownCounter--;
                }

                clickSound.play();
            }
        }
    }

}