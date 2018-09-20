package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.equipment.EquipmentDataComponent;
import com.aesophor.medievania.component.equipment.EquipmentSlot;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.component.item.ItemDataComponent;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.character.ItemEquippedEvent;
import com.aesophor.medievania.event.character.UnequipItemEvent;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class EquipmentTable extends Table implements MenuItemTable {

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
            this.equipmentNameLabel.setText((item != null) ? Mappers.ITEM_DATA.get(item).getName() + "  " : "----");
        }

        public EquipmentType getType() {
            return type;
        }

        public void setSelected(boolean selected) {
            selectedImage.setVisible(selected);
        }

    }

    private final Texture regularItemTexture;
    private final Texture selectedItemTexture;

    private final Array<EquipmentItem> items;
    private int currentItemIdx;

    private final DialogTable dialogTable;

    public EquipmentTable(AssetManager assets, Player player, DialogTable dialogTable) {
        regularItemTexture = assets.get("interface/item_regular.png");
        selectedItemTexture = assets.get("interface/selection.png");

        this.dialogTable = dialogTable;

        items = new Array<>(7);

        top().left();
        setFillParent(true);
        setPosition(50 + 8, -60);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            items.add(new EquipmentItem(slot.getType(), regularItemTexture, selectedItemTexture, LabelStyles.WHITE_HEADER));
        }

        defaults().spaceTop(11f);
        items.forEach(i -> add(i).row());
        items.first().setSelected(true);


        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_EQUIPPED, (ItemEquippedEvent e) -> {
            EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());
            items.get(equipmentData.getType().ordinal()).setItem(e.getItem());
        });

        GameEventManager.getInstance().addEventListener(GameEventType.UNEQUIP_ITEM, (UnequipItemEvent e) -> {
            EquipmentDataComponent equipmentData = Mappers.EQUIPMENT_DATA.get(e.getItem());
            items.get(equipmentData.getType().ordinal()).setItem(null);
            player
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
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (currentItemIdx < items.size - 1) {
                items.get(currentItemIdx).setSelected(false);
                currentItemIdx++;
                items.get(currentItemIdx).setSelected(true);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            dialogTable.show("Unequip this item?", "Yes", "No", new UnequipItemEvent(getSelectedItem().getItem()), null);
        }
    }

    @Override
    public Texture getBackgroundTexture() {
        return null;
    }

}