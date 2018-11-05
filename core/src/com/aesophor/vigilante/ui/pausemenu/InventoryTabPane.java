package com.aesophor.vigilante.ui.pausemenu;

import com.aesophor.vigilante.GameAssetManager;
import com.aesophor.vigilante.component.Mappers;
import com.aesophor.vigilante.component.equipment.EquipmentType;
import com.aesophor.vigilante.component.item.ItemDataComponent;
import com.aesophor.vigilante.component.item.ItemType;
import com.aesophor.vigilante.entity.character.Player;
import com.aesophor.vigilante.entity.item.Item;
import com.aesophor.vigilante.event.GameEventListener;
import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.event.character.InventoryChangedEvent;
import com.aesophor.vigilante.event.ui.MenuDialogOptionEvent;
import com.aesophor.vigilante.event.ui.InventoryItemChangedEvent;
import com.aesophor.vigilante.event.ui.InventoryTabChangedEvent;
import com.aesophor.vigilante.ui.component.InventoryTabs;
import com.aesophor.vigilante.ui.component.ItemListView;
import com.aesophor.vigilante.ui.theme.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class InventoryTabPane extends Pane {

    private static final float Y_CORRECTION = 50f;
    private static final float TABS_PAD_LEFT = 2f;
    private static final float LIST_VIEW_PAD_LEFT = 6f;
    private static final float DESC_PAD_LEFT = 8f;
    private static final float LIST_VIEW_DESC_GAP = 13f;

    private final InventoryTabs inventoryTabs;
    private final ItemListView itemListView;
    private final Label itemDesc;

    private boolean isEquipmentSelector;
    private EquipmentType selectingEquipmentType;

    private final GameEventListener<MenuDialogOptionEvent> promptDiscardItemEvLstnr;

    public InventoryTabPane(AssetManager assets, Player player, MenuDialog menuDialog, float x, float y, float width, float height) {
        super(assets, player, menuDialog, x, y);

        // Initialize assets.
        paneBackgroundTexture = assets.get(GameAssetManager.INVENTORY_BG);

        // Initialize UI components.
        inventoryTabs = new InventoryTabs(assets);
        itemListView = new ItemListView(assets, width, height);
        itemDesc = new Label("", LabelStyles.WHITE_REGULAR);
        itemDesc.setWrap(true);

        padBottom(Y_CORRECTION);
        add(inventoryTabs).padLeft(TABS_PAD_LEFT).left().row();
        add(itemListView).padLeft(LIST_VIEW_PAD_LEFT).width(width).height(height).row();
        add(itemDesc).padLeft(DESC_PAD_LEFT).top().left().width(width).spaceTop(LIST_VIEW_DESC_GAP);

        // Show all equipment in player inventory by default.
        itemListView.populate(Mappers.INVENTORY.get(player), ItemType.EQUIP);


        promptDiscardItemEvLstnr = (MenuDialogOptionEvent e) -> {
            menuDialog.show("Do you want to discard this item?", "Yes", "No", (MenuDialogOptionEvent discardItem) -> {
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
        this.isEquipmentSelector = selectingEquipment;
        this.selectingEquipmentType = equipmentType;
        inventoryTabs.select(ItemType.EQUIP);
    }

    @Override
    public void handleInput(float delta) {
        // Disable user's control over inventory tabs if selecting equipment.
        if (!isEquipmentSelector) {
            inventoryTabs.handleInput(delta);
        }

        // Handles user's control over item list view.
        itemListView.handleInput(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (isEquipmentSelector) {
                if (itemListView.getSelectedItem() != null) {
                    player.equip(itemListView.getSelectedItem());
                } else {
                    player.unequip(selectingEquipmentType);
                    GameEventManager.getInstance().fireEvent(new InventoryChangedEvent());
                }
                isEquipmentSelector = false;
                MenuPage.show(MenuPage.EQUIPMENT);
            } else {
                if (itemListView.getSelectedItem() != null) {
                    switch (itemListView.getSelectedItem().getType()) {
                        case EQUIP:
                            menuDialog.show("", "Equip", "Discard", (MenuDialogOptionEvent equipItem) -> {
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
    }

}