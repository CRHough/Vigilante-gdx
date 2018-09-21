package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.item.ItemDataComponent;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.character.DiscardItemEvent;
import com.aesophor.medievania.event.character.EquipItemEvent;
import com.aesophor.medievania.event.character.InventoryChangedEvent;
import com.aesophor.medievania.event.combat.ItemPickedUpEvent;
import com.aesophor.medievania.event.ui.InventoryItemChangedEvent;
import com.aesophor.medievania.event.ui.InventoryTabChangedEvent;
import com.aesophor.medievania.event.ui.PromptDiscardItemEvent;
import com.aesophor.medievania.ui.InventoryTabs;
import com.aesophor.medievania.ui.LabelStyles;
import com.aesophor.medievania.ui.ItemListView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class InventoryTabPane extends Table implements MenuItemTable {

    private final Texture inventoryBackground;
    private final Sound equipSound;

    private final InventoryTabs inventoryTabs;
    private final ItemListView itemListView;
    private final Label itemDesc;

    private final DialogTable dialogTable;

    public InventoryTabPane(AssetManager assets, Player player, DialogTable dialogTable) {
        this.inventoryTabs = new InventoryTabs(assets);
        this.itemListView = new ItemListView(assets, player);
        this.dialogTable = dialogTable;

        inventoryBackground = assets.get("interface/inventory_bg.png");
        equipSound = assets.get("sfx/inventory/equip.wav", Sound.class);

        itemDesc = new Label("", LabelStyles.WHITE_REGULAR);
        itemDesc.setWrap(true);

        top().left();
        setFillParent(true);
        setPosition(50 + 2, -45);

        defaults().padLeft(5f);
        add(inventoryTabs).align(Align.left).padLeft(0).row();
        add(itemListView.getScrollPane()).width(270f).height(120f).row();
        add(itemDesc).width(270f).top().left().spaceTop(13f);

        // Show all equipment in player inventory by default.
        itemListView.populate(ItemType.EQUIP, player);


        // Add the item which the player has just picked up to inventory content table
        // iff currently selected tab matches the item type.
        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_PICKED_UP, (ItemPickedUpEvent e) -> {
            if (inventoryTabs.getSelectedTab().getType() == e.getItem().getType()) {
                itemListView.add(e.getItem());
            }
        });

        // Clear and re-populate inventory content table with the item type of the newly selected tab.
        GameEventManager.getInstance().addEventListener(GameEventType.INVENTORY_TAB_SELECTED, (InventoryTabChangedEvent e) -> {
            itemListView.clear();
            itemDesc.setText("");
            itemListView.populate(e.getNewTabItemType(), player);
        });

        // Display the description of the newly selected item.
        GameEventManager.getInstance().addEventListener(GameEventType.INVENTORY_ITEM_SELECTED, (InventoryItemChangedEvent e) -> {
            ItemDataComponent itemData = Mappers.ITEM_DATA.get(e.getNewItem());
            itemDesc.setText(itemData.getDesc());
        });

        // Remove the item when the player has confirmed to discard it.
        GameEventManager.getInstance().addEventListener(GameEventType.DISCARD_ITEM, (DiscardItemEvent e) -> {
            Mappers.INVENTORY.get(player).remove(e.getItem());
        });

        GameEventManager.getInstance().addEventListener(GameEventType.EQUIP_ITEM, (EquipItemEvent e) -> {
            player.equip(e.getItem());
            equipSound.play();
        });

        // Whenever there's a change in inventory, refresh the list. (TODO: inventory onChange efficiency)
        GameEventManager.getInstance().addEventListener(GameEventType.INVENTORY_CHANGED, (InventoryChangedEvent e) -> {
            itemListView.clear();
            itemDesc.setText("");
            itemListView.populate(inventoryTabs.getSelectedTab().getType(), player);
        });

        GameEventManager.getInstance().addEventListener(GameEventType.PROMPT_DISCARD_ITEM, (PromptDiscardItemEvent e) -> {
            dialogTable.show("Do you want to discard this item?", "Yes", "No", new DiscardItemEvent(itemListView.getSelectedItem()), null);
        });
    }


    @Override
    public void handleInput(float delta) {
        inventoryTabs.handleInput(delta);
        itemListView.handleInput(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (itemListView.getSelectedItem() != null) {
                switch (itemListView.getSelectedItem().getType()) {
                    case EQUIP:
                        dialogTable.show("", "Equip", "Discard", new EquipItemEvent(itemListView.getSelectedItem()), new PromptDiscardItemEvent());
                        break;

                    case USE:
                        dialogTable.show("", "Use", "Discard", new EquipItemEvent(itemListView.getSelectedItem()), new PromptDiscardItemEvent());
                        break;

                    case MISC:
                    default:
                        dialogTable.show("", "Use", "Discard", null, new PromptDiscardItemEvent());
                        break;
                }
            }
        }
    }

    @Override
    public Texture getBackgroundTexture() {
        return inventoryBackground;
    }

}