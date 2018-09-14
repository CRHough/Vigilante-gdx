package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.component.ItemType;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class InventoryTabTable extends Table implements MenuItemTable {

    private class Tab extends Stack {

        private Image tabImage;
        private Label titleLabel;

        public Tab(Texture tabTexture, String title, Label.LabelStyle labelStyle) {
            tabImage = new Image(tabTexture);
            titleLabel = new Label(title, labelStyle);
            add(tabImage);
            add(titleLabel);
        }

        public void setSelected(boolean selected) {
            getChildren().first().setColor((selected) ? Color.FIREBRICK : Color.WHITE);
        }

    }

    private static final float LABEL_GAP = 1f;

    private final Texture inventoryBackground;
    private final Texture normalTabTexture;
    private final Texture selectedTabTexture;

    private Array<Tab> tabs;
    private int currentItemIdx;

    public InventoryTabTable(GameStateManager gsm) {
        inventoryBackground = gsm.getAssets().get("interface/inventory_bg.png");
        normalTabTexture = gsm.getAssets().get("interface/tab_normal.png");
        selectedTabTexture = gsm.getAssets().get("interface/tab_selected.png");


        top().left();
        setFillParent(true);
        setBounds(50 + 2, -39, inventoryBackground.getWidth(), inventoryBackground.getHeight());

        defaults().padRight(LABEL_GAP);

        tabs = new Array<>(ItemType.values().length);
        tabs.add(new Tab(normalTabTexture, " EQUIP ", LabelStyles.WHITE));
        tabs.add(new Tab(normalTabTexture, "  USE ", LabelStyles.WHITE));
        tabs.add(new Tab(normalTabTexture, " MISC ", LabelStyles.WHITE));
        tabs.add(new Tab(normalTabTexture, " SPECIAL ", LabelStyles.WHITE));

        // Add all tabs to inventory table.
        tabs.forEach(this::add);

        tabs.first().setSelected(true);
    }


    @Override
    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (currentItemIdx > 0) {
                tabs.get(currentItemIdx).setSelected(false);
                currentItemIdx--;
                tabs.get(currentItemIdx).setSelected(true);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (currentItemIdx < ItemType.values().length - 1) {
                tabs.get(currentItemIdx).setSelected(false);
                currentItemIdx++;
                tabs.get(currentItemIdx).setSelected(true);
            }
        }
    }

    @Override
    public Texture getBackgroundTexture() {
        return inventoryBackground;
    }

}