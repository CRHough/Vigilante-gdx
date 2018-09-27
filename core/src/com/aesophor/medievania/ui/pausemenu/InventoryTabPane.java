package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.Asset;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.component.item.ItemDataComponent;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventListener;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.character.InventoryChangedEvent;
import com.aesophor.medievania.event.ui.DialogOptionEvent;
import com.aesophor.medievania.event.ui.InventoryItemChangedEvent;
import com.aesophor.medievania.event.ui.InventoryTabChangedEvent;
import com.aesophor.medievania.ui.component.InventoryTabs;
import com.aesophor.medievania.ui.component.ItemListView;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InventoryTabPane extends Table implements MenuPagePane {

    private static final float TAB_PANE_X = 50 + 2;
    private static final float TAB_PANE_Y = -45;

    private final Texture inventoryBackground;

    private final InventoryTabs inventoryTabs;
    private final ItemListView itemListView;
    private final Label itemDesc;

    private final MenuDialog menuDialog;

    private Player player;
    private boolean isSelectingEquipment; /* InventoryTabPane can also be used as an equipment selector! */
    private EquipmentType equipmentType;
    private final GameEventListener<DialogOptionEvent> promptDiscardItemEvLstnr;

    public InventoryTabPane(AssetManager assets, Player player, MenuDialog menuDialog) {
        this.inventoryTabs = new InventoryTabs(assets);
        this.itemListView = new ItemListView(assets);
        this.menuDialog = menuDialog;
        this.player = player;

        inventoryBackground = assets.get(Asset.INVENTORY_BG);

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


        promptDiscardItemEvLstnr = (DialogOptionEvent e) -> {
            menuDialog.show("Do you want to discard this item?", "Yes", "No", (DialogOptionEvent discardItem) -> {
                Item selectedItem = itemListView.getSelectedItem();
                player.discard(selectedItem);
            }, null);
        };


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

        // Whenever there's a change in inventory, refresh the list.
        GameEventManager.getInstance().addEventListener(GameEventType.INVENTORY_CHANGED, (InventoryChangedEvent e) -> {
            itemListView.clear();
            itemDesc.setText("");
            itemListView.populate(Mappers.INVENTORY.get(player), inventoryTabs.getSelectedTab().getType());
        });
    }


    public ItemListView getItemListView() {
        return itemListView;
    }

    public void setSelectingEquipment(boolean selectingEquipment, EquipmentType equipmentType) {
        this.isSelectingEquipment = selectingEquipment;
        this.equipmentType = equipmentType;
        inventoryTabs.select(ItemType.EQUIP);
    }

    @Override
    public void handleInput(float delta) {
        // Disable user's control over inventory tabs if selecting equipment.
        if (!isSelectingEquipment) {
            inventoryTabs.handleInput(delta);
        }

        // Handles user's control over item list view.
        itemListView.handleInput(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (isSelectingEquipment) {
                if (itemListView.getSelectedItem() != null) {
                    player.equip(itemListView.getSelectedItem());
                } else {
                    player.unequip(equipmentType);
                    GameEventManager.getInstance().fireEvent(new InventoryChangedEvent());
                }
                isSelectingEquipment = false;
                MenuPage.show(MenuPage.EQUIPMENT);
            } else {
                switch (itemListView.getSelectedItem().getType()) {
                    case EQUIP:
                        menuDialog.show("", "Equip", "Discard", (DialogOptionEvent equipItem) -> {
                            Item selectedItem = itemListView.getSelectedItem();
                            player.equip(selectedItem);
                        }, promptDiscardItemEvLstnr);
                        break;

                    case USE:
                        //menuDialog.show("", "Use", "Discard", new UseItemEvent(player, itemListView.getSelectedItem()), promptDiscardItemEvLstnr);
                        break;

                    case MISC:
                    default:
                        //menuDialog.show("", "Use", "Discard", null, promptDiscardItemEvLstnr);
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