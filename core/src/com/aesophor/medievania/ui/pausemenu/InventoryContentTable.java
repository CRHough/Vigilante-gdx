package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;

public class InventoryContentTable extends Table implements MenuItemTable {

    private class InventoryItem extends Stack {

        private Image selectionImage;
        private Label nameLabel;

        public InventoryItem(Texture selectionTexture, String title, Label.LabelStyle labelStyle) {
            selectionImage = new Image(selectionTexture);
            nameLabel = new Label(" " + title , labelStyle);
            setSelected(false);

            add(this.selectionImage);
            add(nameLabel);
        }

        public void setSelected(boolean selected) {
            selectionImage.setVisible(selected);
        }

    }

    private Texture selectionTexture;

    private ScrollPane scrollPane;
    private Table innerTable;

    private Array<InventoryItem> items;
    private int currentItemIdx;
    private int navigateUpCounter;
    private int navigateDownCounter;

    private Label itemDesc;

    public InventoryContentTable(GameStateManager gsm) {
        selectionTexture = gsm.getAssets().get("interface/selection.png");

        top().left();
        setPosition(50 + 8, -66);
        setFillParent(true);

        innerTable = new Table();
        innerTable.top().left();
        scrollPane = new ScrollPane(innerTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);
        add(scrollPane).width(270f).height(120f);
        row();

        // Ah fuck reflect the player's inventory here tomorrow I'm too tired today
        items = new Array<>();
        innerTable.defaults().spaceTop(5f).left();
        items.add(new InventoryItem(selectionTexture, "Rusty Saber", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Chain Mail", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Leather Boots", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Hide Gauntlets", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Crimson Cape", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Some stuff", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Some other stuff", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Another stuff", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Unamed itemData", LabelStyles.WHITE));
        items.add(new InventoryItem(selectionTexture, "Doge", LabelStyles.WHITE));

        items.forEach(item -> innerTable.add(item).row());
        items.first().setSelected(true);

        itemDesc = new Label("A broken saber that needs repair.", LabelStyles.WHITE);
        itemDesc.setWrap(true);
        add(itemDesc).width(270f).top().left().spaceTop(13f);
    }


    @Override
    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (currentItemIdx > 0) {
                // Highlight the previous itemData.
                items.get(currentItemIdx).setSelected(false);
                currentItemIdx--;
                items.get(currentItemIdx).setSelected(true);

                // Decrement the navigateDownCounter so that if user suddenly navigate downward,
                // the scrollpane can scroll down correctly later.
                if (navigateDownCounter > 0) {
                    navigateDownCounter--;
                }

                navigateUpCounter++;

                if (navigateUpCounter >= 5) {
                    scrollPane.setScrollY(scrollPane.getScrollY() - 24f);
                    navigateUpCounter--;
                }
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (currentItemIdx < items.size - 1) {
                // Highlight the next itemData.
                items.get(currentItemIdx).setSelected(false);
                currentItemIdx++;
                items.get(currentItemIdx).setSelected(true);

                // Increment the navigateUpCounter so that if user suddenly navigate upward,
                // the scrollpane can scroll up correctly later.
                if (navigateUpCounter > 0) {
                    navigateUpCounter--;
                }

                navigateDownCounter++;

                if (navigateDownCounter >= 5) {
                    scrollPane.setScrollY(scrollPane.getScrollY() + 24f);
                    navigateDownCounter--;
                }
            }
        }
    }

    @Override
    public Texture getBackgroundTexture() {
        return null;
    }
}
