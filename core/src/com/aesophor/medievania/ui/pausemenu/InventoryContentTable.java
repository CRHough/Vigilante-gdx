package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InventoryContentTable extends Table implements MenuItemTable {

    private Texture selectionTexture;

    private ScrollPane scrollPane;
    private Table innerTable;

    public InventoryContentTable(GameStateManager gsm) {
        selectionTexture = gsm.getAssets().get("interface/selection.png");

        top().left();
        setPosition(50 + 8, -60);
        setFillParent(true);



        innerTable = new Table();
        innerTable.top().left();
        scrollPane = new ScrollPane(innerTable);
        add(scrollPane).width(260f).height(120f);
        row();


        innerTable.defaults().spaceTop(5f);
        innerTable.add(new Label("Alucard's Sword", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Club", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Quilted Mail", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Alucard's Sword", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Club", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Quilted Mail", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Alucard's Sword", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Club", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Quilted Mail", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Alucard's Sword", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Club", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Quilted Mail", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Alucard's Sword", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Club", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Quilted Mail", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Alucard's Sword", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Club", LabelStyles.WHITE)).left().row();
        innerTable.add(new Label("Quilted Mail", LabelStyles.WHITE)).left().row();
    }


    @Override
    public void handleInput(float delta) {

    }

    @Override
    public Texture getBackgroundTexture() {
        return null;
    }
}
