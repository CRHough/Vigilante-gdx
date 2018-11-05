package com.aesophor.vigilante.ui.component;

import com.aesophor.vigilante.GameAssetManager;
import com.aesophor.vigilante.component.item.ItemType;
import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.ui.InventoryTabChangedEvent;
import com.aesophor.vigilante.ui.theme.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class InventoryTabs extends HorizontalGroup {

    public class InventoryTab extends Stack {

        private ItemType type;
        private Image regularTabImage;
        private Image selectedTabImage;
        private Label titleLabel;

        public InventoryTab(ItemType type, Texture regularTabTexture, Texture selectedTabTexture, Label.LabelStyle labelStyle) {
            this.type = type;
            this.regularTabImage = new Image(regularTabTexture);
            this.selectedTabImage = new Image(selectedTabTexture);
            this.titleLabel = new Label(type.name(), labelStyle);
            this.titleLabel.setAlignment(Align.center);

            this.add(regularTabImage);
            this.add(selectedTabImage);
            this.add(titleLabel);
            this.setSelected(false);
        }

        public void setSelected(boolean selected) {
            selectedTabImage.setVisible(selected);
        }

        public ItemType getType() {
            return type;
        }

    }

    private final Texture regularTabTexture;
    private final Texture selectedTabTexture;
    private final Sound clickSound;

    private Array<InventoryTab> tabs;
    private int currentItemIdx;

    public InventoryTabs(AssetManager assets) {
        regularTabTexture = assets.get(GameAssetManager.TAB_REGULAR);
        selectedTabTexture = assets.get(GameAssetManager.TAB_HIGHLIGHTED);
        clickSound = assets.get(GameAssetManager.UI_CLICK_SOUND, Sound.class);

        space(1f);      /* Space between tabs */
        padBottom(5f);  /* Padding between tabs and whatever content below them. */

        tabs = new Array<>(ItemType.values().length);
        for (ItemType itemType : ItemType.values()) {
            tabs.add(new InventoryTab(itemType, regularTabTexture, selectedTabTexture, LabelStyles.WHITE_HEADER));
        }
        tabs.forEach(this::addActor);
        tabs.first().setSelected(true);
    }


    /**
     * Gets the selected tab.
     * @return selected tab.
     */
    public InventoryTab getSelectedTab() {
        return tabs.get(currentItemIdx);
    }

    public void select(ItemType itemType) {
       tabs.get(currentItemIdx).setSelected(false);
       currentItemIdx = itemType.ordinal();
       tabs.get(currentItemIdx).setSelected(true);
    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (currentItemIdx > 0) {
                select(ItemType.values()[currentItemIdx - 1]);
                GameEventManager.getInstance().fireEvent(new InventoryTabChangedEvent(tabs.get(currentItemIdx).getType()));
                clickSound.play();
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (currentItemIdx < ItemType.values().length - 1) {
                select(ItemType.values()[currentItemIdx + 1]);
                GameEventManager.getInstance().fireEvent(new InventoryTabChangedEvent(tabs.get(currentItemIdx).getType()));
                clickSound.play();
            }
        }
    }

}
