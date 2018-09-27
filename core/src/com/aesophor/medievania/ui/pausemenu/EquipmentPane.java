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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class EquipmentPane extends Pane {

    private class EquipmentItem extends Stack {

        private static final String EMPTY_ITEM_TEXT = "----"; // The name to display on empty item's label.
        private static final float PADDING = 5f; // Left / right padding.
        private static final float SPACING = 7f; // Space between each of its content (icon and label).
        private final Image regularItemImage;
        private final Image highlightedItemImage;
        private final EquipmentType type;

        private Item item;
        private final Label slotNameLabel;
        private final Image equipmentIconImage;
        private final Label equipmentNameLabel;

        public EquipmentItem(EquipmentType type, Label.LabelStyle labelStyle) {
            this.type = type;
            this.regularItemImage = new Image(regularEquipmentItemTexture);
            this.highlightedItemImage = new Image(highlightedEquipmentItemTexture);

            this.slotNameLabel = new Label(type.name(), labelStyle);
            this.equipmentNameLabel = new Label(EMPTY_ITEM_TEXT, LabelStyles.WHITE_REGULAR); // clean this up later.
            this.equipmentIconImage = new Image((item != null) ? Mappers.SPRITE.get(item) : new Sprite(emptyItemIconTexture));
            this.slotNameLabel.setAlignment(Align.left);
            this.equipmentNameLabel.setAlignment(Align.right);

            HorizontalGroup equipmentNameGroup = new HorizontalGroup();
            equipmentNameGroup.align(Align.left);
            equipmentNameGroup.padLeft(PADDING);
            equipmentNameGroup.space(SPACING);
            equipmentNameGroup.addActor(equipmentIconImage);
            equipmentNameGroup.addActor(slotNameLabel);

            HorizontalGroup itemNameGroup = new HorizontalGroup();
            itemNameGroup.align(Align.left);
            itemNameGroup.padLeft(105 + PADDING);
            itemNameGroup.padRight(PADDING);
            itemNameGroup.addActor(equipmentNameLabel);

            this.add(regularItemImage);
            this.add(highlightedItemImage);
            this.add(equipmentNameGroup);
            this.add(itemNameGroup);

            this.setSelected(false);
        }


        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
            this.equipmentIconImage.setDrawable(new TextureRegionDrawable((item != null) ? Mappers.SPRITE.get(item) : new Sprite(emptyItemIconTexture)));
            this.equipmentNameLabel.setText((item != null) ? Mappers.ITEM_DATA.get(item).getName() : EMPTY_ITEM_TEXT);
        }

        public EquipmentType getType() {
            return type;
        }

        public void setSelected(boolean selected) {
            highlightedItemImage.setVisible(selected);
        }

    }

    private static final float GAP = 11f;

    private final Texture regularEquipmentItemTexture;
    private final Texture highlightedEquipmentItemTexture;
    private final Texture emptyItemIconTexture;
    private final Sound clickSound;

    private final Array<EquipmentItem> items;
    private int currentItemIdx;

    public EquipmentPane(AssetManager assets, Player player, float x, float y) {
        super(assets, player, x, y);

        regularEquipmentItemTexture = assets.get(Asset.EQUIPMENT_REGULAR);
        highlightedEquipmentItemTexture = assets.get(Asset.EQUIPMENT_HIGHLIGHTED);
        emptyItemIconTexture = assets.get(Asset.EMPTY_ITEM);
        clickSound = assets.get(Asset.UI_CLICK_SOUND, Sound.class);

        items = new Array<>(7);
        for (EquipmentType type : EquipmentType.values()) {
            items.add(new EquipmentItem(type, LabelStyles.WHITE_HEADER));
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

            InventoryTabPane inventoryTabPane = ((InventoryTabPane) MenuPage.INVENTORY.getPanes().first());
            inventoryTabPane.getItemListView().clear();
            inventoryTabPane.getItemListView()
                    .insertEmptyItem()
                    .populate(Mappers.INVENTORY.get(player), ItemType.EQUIP)
                    .filter(getSelectedItem().getType());
            inventoryTabPane.setSelectingEquipment(true, getSelectedItem().getType());

            clickSound.play();
        }
    }

}