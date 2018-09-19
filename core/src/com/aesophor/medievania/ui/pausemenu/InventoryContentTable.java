package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.component.character.InventoryComponent;
import com.aesophor.medievania.component.item.ItemDataComponent;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.combat.ItemPickedUpEvent;
import com.aesophor.medievania.event.ui.InventoryItemChangedEvent;
import com.aesophor.medievania.event.ui.InventoryTabChangedEvent;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;

public class InventoryContentTable extends Table implements MenuItemTable {

    private class InventoryItem extends Stack {

        private Item item;
        private Label nameLabel;
        private Image selectionImage;


        public InventoryItem(Item item, Texture selectionTexture, Label.LabelStyle labelStyle) {
            this.item = item;

            ItemDataComponent itemData = Mappers.ITEM_DATA.get(item);
            nameLabel = new Label(" " + itemData.getName() , labelStyle);

            selectionImage = new Image(selectionTexture);
            setSelected(false);

            add(this.selectionImage);
            add(nameLabel);
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

    private final InventoryTabTable inventoryTabTable;

    private final Texture selectionTexture;
    private final Sound clickSound;

    private ScrollPane scrollPane;
    private Table contentTable;

    private Array<InventoryItem> items;
    private int currentItemIdx;
    private int navigateUpCounter;
    private int navigateDownCounter;

    private Label itemDesc;

    public InventoryContentTable(GameStateManager gsm, Player player, InventoryTabTable inventoryTabTable) {
        this.inventoryTabTable = inventoryTabTable;

        selectionTexture = gsm.getAssets().get("interface/selection.png");
        clickSound = gsm.getAssets().get("sfx/ui/click.wav", Sound.class);

        top().left();
        setPosition(50 + 8, -66);
        setFillParent(true);

        contentTable = new Table();
        contentTable.top().left();
        scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);
        add(scrollPane).width(270f).height(120f);
        row();

        currentItemIdx = -1;


        // Ah fuck reflect the player's inventory here tomorrow I'm too tired today
        items = new Array<>();
        contentTable.defaults().spaceTop(5f).left();

        // Shows all equipment in player inventory by default.
        populate(ItemType.EQUIP, player);

        itemDesc = new Label("", LabelStyles.WHITE_REGULAR);
        itemDesc.setWrap(true);
        add(itemDesc).width(270f).top().left().spaceTop(13f);


        // Ah I'll comment this later.
        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_PICKED_UP, (ItemPickedUpEvent e) -> {
            if (inventoryTabTable.getSelectedTabType() == e.getItem().getType()) {
                add(e.getItem());
            }
        });

        // Repopulate inventory content table with the item type of the newly selected tab.
        GameEventManager.getInstance().addEventListener(GameEventType.INVENTORY_TAB_CHANGED, (InventoryTabChangedEvent e) -> {
            clear();
            populate(e.getNewTabItemType(), player);
        });

        // Display the description of the newly selected item.
        GameEventManager.getInstance().addEventListener(GameEventType.INVENTORY_ITEM_CHANGED, (InventoryItemChangedEvent e) -> {
            ItemDataComponent itemData = Mappers.ITEM_DATA.get(e.getNewItem());
            itemDesc.setText(itemData.getDesc());
        });
    }


    /**
     * Populates the inventory content table with the specified type of items from player inventory.
     * @param type type of items to display.
     * @param player player entity which will be used for retrieving its inventory component.
     */
    public void populate(ItemType type, Player player) {
        InventoryComponent inventory = Mappers.INVENTORY.get(player);

        inventory.get(type).forEach(item -> {
            InventoryContentTable.InventoryItem i = new InventoryItem(item, selectionTexture, LabelStyles.WHITE_REGULAR);
            items.add(i);
            contentTable.add(i).row();
        });

        if (items.size > 0) {
            items.first().setSelected(true);
            GameEventManager.getInstance().fireEvent(new InventoryItemChangedEvent(items.get(currentItemIdx).getItem()));
        }
    }

    /**
     * Adds the specified item to the inventory content table.
     * @param item item to add.
     */
    public void add(Item item) {
        InventoryContentTable.InventoryItem i = new InventoryItem(item, selectionTexture, LabelStyles.WHITE_REGULAR);

        items.add(i);
        contentTable.add(i).row();

        if (getSelectedItem() == null) {
            currentItemIdx = 0;
            items.first().setSelected(true);
        }
    }

    /**
     * Clears the inventory content table.
     */
    public void clear() {
        contentTable.clearChildren();
        items.clear();
        itemDesc.setText("");
    }

    /**
     * Gets the currently selected item.
     * @return selected item.
     */
    public Item getSelectedItem() {
        return (currentItemIdx >= 0) ? items.get(currentItemIdx).getItem() : null;
    }

    @Override
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

    @Override
    public Texture getBackgroundTexture() {
        return null;
    }
}
