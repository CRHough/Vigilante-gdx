package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.Asset;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.equipment.EquipmentDataComponent;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.character.ItemEquippedEvent;
import com.aesophor.medievania.event.character.ItemUnequippedEvent;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class EquipmentPane extends Table implements MenuPagePane {

    private class EquipmentItem extends Stack {

        private final EquipmentType type;
        private Item item;
        private Image regularImage;
        private Image selectedImage;
        private Label slotNameLabel;
        private Label equipmentNameLabel;
        private HorizontalGroup labelGroup;

        public EquipmentItem(EquipmentType type, Texture regularTexture, Texture selectedTexture, Label.LabelStyle labelStyle) {
            this.type = type;
            this.regularImage = new Image(regularTexture);
            this.selectedImage = new Image(selectedTexture);
            this.labelGroup = new HorizontalGroup();

            this.slotNameLabel = new Label("  " + type.name(), labelStyle);
            this.equipmentNameLabel = new Label("----" + "  " , LabelStyles.WHITE_REGULAR); // clean this up later.
            this.slotNameLabel.setAlignment(Align.left);
            this.equipmentNameLabel.setAlignment(Align.right);

            this.labelGroup.addActor(slotNameLabel);
            this.labelGroup.addActor(equipmentNameLabel);
            this.labelGroup.padLeft(5f);
            this.labelGroup.padRight(5f);

            this.add(regularImage);
            this.add(selectedImage);
            this.add(slotNameLabel);
            this.add(equipmentNameLabel);

            this.setSelected(false);
        }


        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
            this.equipmentNameLabel.setText((item != null) ? Mappers.ITEM_DATA.get(item).getName() + "  " : "----" + "  ");
        }

        public EquipmentType getType() {
            return type;
        }

        public void setSelected(boolean selected) {
            selectedImage.setVisible(selected);
        }

    }

    private static final float TAB_PANE_X = 50 + 8;
    private static final float TAB_PANE_Y = -60;
    private static final float GAP = 11f;

    private final Texture regularItemTexture;
    private final Texture selectedItemTexture;
    private final Sound clickSound;

    private final Array<EquipmentItem> items;
    private int currentItemIdx;

    private final Player player;
    private final MenuDialog menuDialog;

    public EquipmentPane(AssetManager assets, Player player, MenuDialog menuDialog) {
        this.player = player;
        this.menuDialog = menuDialog;

        regularItemTexture = assets.get(Asset.ITEM_REGULAR);
        selectedItemTexture = assets.get(Asset.ITEM_HIGHLIGHTED);
        clickSound = assets.get(Asset.UI_CLICK_SOUND, Sound.class);

        top().left();
        setFillParent(true);
        setPosition(TAB_PANE_X, TAB_PANE_Y);

        items = new Array<>(7);
        for (EquipmentType type : EquipmentType.values()) {
            items.add(new EquipmentItem(type, regularItemTexture, selectedItemTexture, LabelStyles.WHITE_HEADER));
        }

        defaults().spaceTop(GAP);
        items.forEach(i -> add(i).row());
        items.first().setSelected(true);


        // Update equipment UI when the player equipped an item.
        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_EQUIPPED, (ItemEquippedEvent e) -> {
            EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());
            items.get(equipmentData.getType().ordinal()).setItem(e.getItem());
        });

        // Update equipment UI when the player unequipped an item.
        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_UNEQUIPPED, (ItemUnequippedEvent e) -> {
            EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());
            items.get(equipmentData.getType().ordinal()).setItem(null);
        });
    }


    public EquipmentItem getSelectedItem() {
        return items.get(currentItemIdx);
    }

    @Override
    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (currentItemIdx > 0) {
                items.get(currentItemIdx).setSelected(false);
                currentItemIdx--;
                items.get(currentItemIdx).setSelected(true);
                clickSound.play();
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (currentItemIdx < items.size - 1) {
                items.get(currentItemIdx).setSelected(false);
                currentItemIdx++;
                items.get(currentItemIdx).setSelected(true);
                clickSound.play();
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // When player hits ENTER on an equipment slot, switch to the inventory page and
            // prompt the player to select an item to put.
            MenuPage.show(MenuPage.INVENTORY);

            InventoryTabPane inventoryTabPane = ((InventoryTabPane) MenuPage.INVENTORY.getTables().first());
            inventoryTabPane.getItemListView().clear();
            inventoryTabPane.getItemListView()
                    .insertEmptyItem()
                    .populate(Mappers.INVENTORY.get(player), ItemType.EQUIP)
                    .filter(getSelectedItem().getType());
            inventoryTabPane.setSelectingEquipment(true, getSelectedItem().getType());

            clickSound.play();
        }
    }

    @Override
    public Texture getBackgroundTexture() {
        return null;
    }

}