package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InventoryTable extends Table {

    private static final float LABEL_GAP = 1f;

    private final Texture inventoryBackground;
    private final Texture normalLabelTexture;
    private final Texture selectedLabelTexture;

    public InventoryTable(GameStateManager gsm) {
        inventoryBackground = gsm.getAssets().get("interface/inventory_bg.png");
        normalLabelTexture = gsm.getAssets().get("interface/label_normal.png");
        selectedLabelTexture = gsm.getAssets().get("interface/label_selected.png");

        BitmapFont bitmapFont = gsm.getFont().getDefaultFont();
        Label.LabelStyle whiteLabelStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);
        Label.LabelStyle greyLabelStyle = new Label.LabelStyle(bitmapFont, Color.GRAY);
        Label.LabelStyle redLabelStyle = new Label.LabelStyle(bitmapFont, Color.FIREBRICK);


        bottom().left();
        setFillParent(true);
        setBounds(50 + 2, 245, inventoryBackground.getWidth(), inventoryBackground.getHeight());

        defaults().padRight(LABEL_GAP);
        Stack equipLabelStack = new Stack();
        equipLabelStack.add(new Image(normalLabelTexture));
        equipLabelStack.add(new Label("EQUIP", whiteLabelStyle));

        Stack consumableLabelStack = new Stack();
        consumableLabelStack.add(new Image(normalLabelTexture));
        consumableLabelStack.add(new Label("CONSUMABLE", whiteLabelStyle));

        Stack miscLabelStack = new Stack();
        miscLabelStack.add(new Image(normalLabelTexture));
        miscLabelStack.add(new Label("MISC", whiteLabelStyle));

        Stack specialLabelStack = new Stack();
        specialLabelStack.add(new Image(normalLabelTexture));
        specialLabelStack.add(new Label("SPECIAL", whiteLabelStyle));

        add(equipLabelStack);
        add(consumableLabelStack);
        add(miscLabelStack);
        add(specialLabelStack);
    }


    public Texture getBackgroundTexture() {
        return inventoryBackground;
    }

}