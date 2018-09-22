package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.component.item.ItemDataComponent;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.character.DiscardItemEvent;
import com.aesophor.medievania.event.character.EquipItemEvent;
import com.aesophor.medievania.event.character.InventoryChangedEvent;
import com.aesophor.medievania.event.character.UnequipItemEvent;
import com.aesophor.medievania.event.combat.ItemPickedUpEvent;
import com.aesophor.medievania.event.ui.InventoryItemChangedEvent;
import com.aesophor.medievania.event.ui.InventoryTabChangedEvent;
import com.aesophor.medievania.event.ui.PromptDiscardItemEvent;
import com.aesophor.medievania.ui.InventoryTabs;
import com.aesophor.medievania.ui.ItemListView;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InventoryTabPane extends Table implements MenuPagePane {

    private static final float TAB_PANE_X = 50 + 2;
    private static final float TAB_PANE_Y = -45;

    private final Texture inventoryBackground;
    private final Sound equipSound;

    private final InventoryTabs inventoryTabs;
    private final ItemListView itemListView;
    private final Label itemDesc;

    private final MenuDialog menuDialog;

    private Player player;
    private boolean selectingEquipment; /* InventoryTabPane can also be used as an equipment selector! */
    private EquipmentType equipmentType;

    public InventoryTabPane(AssetManager assets, Player player, MenuDialog menuDialog) {
        this.inventoryTabs = new InventoryTabs(assets);
        this.itemListView = new ItemListView(assets);
        this.menuDialog = menuDialog;
        this.player = player;

        inventoryBackground = assets.get("interface/inventory_bg.png");
        equipSound = assets.get("sfx/inventory/equip.wav", Sound.class);

        itemDesc = new Label("", LabelStyles.WHITE_REGULAR);
        itemDesc.setWrap(true);

        top().left();
        setFillParent(true);
        setPosition(TAB_PANE_X, TAB_PANE_Y);

        defaults().padLeft(5f);
        add(inventoryTabs).padLeft(0).left().row();
        add(itemListView).width(270f).height(120f).row();
        add(itemDesc).width(270f).top().left().spaceTop(13f);

        // Show all equipment in player inventory by default.
        itemListView.populate(Mappers.INVENTORY.get(player), ItemType.EQUIP);


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
            itemListView.populate(Mappers.INVENTORY.get(player), e.getNewTabItemType());
        });

        // Display the description of the newly selected item.
        GameEventManager.getInstance().addEventListener(GameEventType.INVENTORY_ITEM_SELECTED, (InventoryItemChangedEvent e) -> {
            if (e.getNewItem() != null) {
                ItemDataComponent itemData = Mappers.ITEM_DATA.get(e.getNewItem());
                itemDesc.setText(itemData.getDesc());
            } else {
                itemDesc.setText("Unequip current item.");
            }
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
            itemListView.populate(Mappers.INVENTORY.get(player), inventoryTabs.getSelectedTab().getType());
        });

        GameEventManager.getInstance().addEventListener(GameEventType.PROMPT_DISCARD_ITEM, (PromptDiscardItemEvent e) -> {
            menuDialog.show("Do you want to discard this item?", "Yes", "No", new DiscardItemEvent(itemListView.getSelectedItem()), null);
        });
    }


    public ItemListView getItemListView() {
        return itemListView;
    }

    public void setSelectingEquipment(boolean selectingEquipment, EquipmentType equipmentType) {
        this.selectingEquipment = selectingEquipment;
        this.equipmentType = equipmentType;
        inventoryTabs.select(ItemType.EQUIP);
    }

    @Override
    public void handleInput(float delta) {
        if (!selectingEquipment) {
            inventoryTabs.handleInput(delta);
        }
        itemListView.handleInput(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (selectingEquipment) {
                if (itemListView.getSelectedItem() != null) {
                    player.equip(getItemListView().getSelectedItem());
                } else {
                    if (Mappers.EQUIPMENT_SLOTS.get(player).get(equipmentType) != null) {
                        GameEventManager.getInstance().fireEvent(new UnequipItemEvent(Mappers.EQUIPMENT_SLOTS.get(player).get(equipmentType)));
                    }
                    GameEventManager.getInstance().fireEvent(new InventoryChangedEvent());
                }
                selectingEquipment = false;
                MenuPage.show(MenuPage.EQUIPMENT);
            } else {
                switch (itemListView.getSelectedItem().getType()) {
                    case EQUIP:
                        menuDialog.show("", "Equip", "Discard", new EquipItemEvent(itemListView.getSelectedItem()), new PromptDiscardItemEvent());
                        break;

                    case USE:
                        menuDialog.show("", "Use", "Discard", new EquipItemEvent(itemListView.getSelectedItem()), new PromptDiscardItemEvent());
                        break;

                    case MISC:
                    default:
                        menuDialog.show("", "Use", "Discard", null, new PromptDiscardItemEvent());
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