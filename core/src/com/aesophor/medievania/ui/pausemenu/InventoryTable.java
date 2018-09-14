package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InventoryTable extends Table implements MenuItemTable {

    private static final float LABEL_GAP = 1f;

    private final Texture inventoryBackground;
    private final Texture normalLabelTexture;
    private final Texture selectedLabelTexture;

    public InventoryTable(GameStateManager gsm) {
        inventoryBackground = gsm.getAssets().get("interface/inventory_bg.png");
        normalLabelTexture = gsm.getAssets().get("interface/label_normal.png");
        selectedLabelTexture = gsm.getAssets().get("interface/label_selected.png");


        bottom().left();
        setFillParent(true);
        setBounds(50 + 2, 245, inventoryBackground.getWidth(), inventoryBackground.getHeight());

        defaults().padRight(LABEL_GAP);
        Stack equipLabelStack = new Stack();
        equipLabelStack.add(new Image(normalLabelTexture));
        equipLabelStack.add(new Label(" EQUIP ", LabelStyles.WHITE));

        Stack consumableLabelStack = new Stack();
        consumableLabelStack.add(new Image(normalLabelTexture));
        consumableLabelStack.add(new Label("  USE ", LabelStyles.WHITE));

        Stack miscLabelStack = new Stack();
        miscLabelStack.add(new Image(normalLabelTexture));
        miscLabelStack.add(new Label(" MISC ", LabelStyles.WHITE));

        Stack specialLabelStack = new Stack();
        specialLabelStack.add(new Image(normalLabelTexture));
        specialLabelStack.add(new Label(" SPECIAL ", LabelStyles.WHITE));

        add(equipLabelStack);
        add(consumableLabelStack);
        add(miscLabelStack);
        add(specialLabelStack);
    }


    @Override
    public void handleInput(float delta) {

    }

    @Override
    public Texture getBackgroundTexture() {
        return inventoryBackground;
    }

}