package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.ui.InventoryTabChangedEvent;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class InventoryTabTable extends Table implements MenuItemTable {

    private static class Tab extends Stack {

        private ItemType type;
        private Image regularTabImage;
        private Image selectedTabImage;
        private Label titleLabel;

        public Tab(ItemType type, Texture regularTabTexture, Texture selectedTabTexture, Label.LabelStyle labelStyle) {
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

    private static final float LABEL_GAP = 1f;

    private final Texture inventoryBackground;
    private final Texture normalTabTexture;
    private final Texture selectedTabTexture;
    private final Sound clickSound;

    private Array<Tab> tabs;
    private int currentItemIdx;

    public InventoryTabTable(GameStateManager gsm) {
        inventoryBackground = gsm.getAssets().get("interface/inventory_bg.png");
        normalTabTexture = gsm.getAssets().get("interface/tab_normal.png");
        selectedTabTexture = gsm.getAssets().get("interface/tab_selected.png");
        clickSound = gsm.getAssets().get("sfx/ui/click.wav", Sound.class);

        top().left();
        setFillParent(true);
        setBounds(50 + 2, -45, inventoryBackground.getWidth(), inventoryBackground.getHeight());

        defaults().padRight(LABEL_GAP);

        tabs = new Array<>(ItemType.values().length);
        for (ItemType itemType : ItemType.values()) {
            tabs.add(new Tab(itemType, normalTabTexture, selectedTabTexture, LabelStyles.WHITE_HEADER));
        }

        // Add all tabs to inventory table.
        tabs.forEach(this::add);

        tabs.first().setSelected(true);
    }


    public ItemType getSelectedTabType() {
        return tabs.get(currentItemIdx).getType();
    }

    @Override
    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (currentItemIdx > 0) {
                tabs.get(currentItemIdx).setSelected(false);
                currentItemIdx--;
                tabs.get(currentItemIdx).setSelected(true);
                GameEventManager.getInstance().fireEvent(new InventoryTabChangedEvent(tabs.get(currentItemIdx).getType()));
                clickSound.play();
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (currentItemIdx < ItemType.values().length - 1) {
                tabs.get(currentItemIdx).setSelected(false);
                currentItemIdx++;
                tabs.get(currentItemIdx).setSelected(true);
                GameEventManager.getInstance().fireEvent(new InventoryTabChangedEvent(tabs.get(currentItemIdx).getType()));
                clickSound.play();
            }
        }
    }

    @Override
    public Texture getBackgroundTexture() {
        return inventoryBackground;
    }

}