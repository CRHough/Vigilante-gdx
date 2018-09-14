package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseMenu extends Stage {

    private final GameStateManager gsm;
    private Player player;

    private Texture background;

    private StatsTable statsTable;
    private InventoryTable inventoryTable;

    public PauseMenu(GameStateManager gsm) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;

        background = gsm.getAssets().get("interface/pause.png");

        Table optionsTable = new Table();
        optionsTable.top().padTop(10f);
        optionsTable.defaults().padRight(30f);
        optionsTable.setFillParent(true);

        MenuItem.buildLabels().forEach(optionsTable::add);

        statsTable = new StatsTable(gsm); // pass player into StatsTable.
        inventoryTable = new InventoryTable(gsm);

        addActor(optionsTable);
        addActor(statsTable);
        addActor(inventoryTable);
    }


    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            MenuItem.next();
            show(MenuItem.current());
        }
    }

    public void update(float delta) {
        handleInput(delta);
        act(delta);

        gsm.getBatch().begin();
        gsm.getBatch().draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        gsm.getBatch().draw(statsTable.getBackgroundTexture(), 380, 46);
        if (MenuItem.current() == MenuItem.INVENTORY) gsm.getBatch().draw(inventoryTable.getBackgroundTexture(), 50, 46);
        gsm.getBatch().end();

        MenuItem.updateLabelColors();
    }

    private void show(MenuItem currentItem) {
        switch (currentItem) {
            case INVENTORY:
                inventoryTable.setVisible(true);
                break;

            case EQUIPMENT:
                inventoryTable.setVisible(false);
                break;

            case SKILLS:
                break;

            case JOURNAL:
                break;

            case OPTIONS:
                break;

            default:
                break;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}