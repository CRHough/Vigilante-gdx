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
    private final Texture background;

    private final StatsTable statsTable;

    public PauseMenu(GameStateManager gsm, Player player) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;

        background = gsm.getAssets().get("interface/pause.png");

        Table menuItemHeaderTable = new Table();
        menuItemHeaderTable.top().padTop(10f);
        menuItemHeaderTable.defaults().padRight(30f);
        menuItemHeaderTable.setFillParent(true);
        MenuItem.buildLabels().forEach(menuItemHeaderTable::add);

        statsTable = new StatsTable(gsm, player);

        MenuItem.INVENTORY.setTable(new InventoryTable(gsm));
        //MenuItem.EQUIPMENT.setTable();
        //MenuItem.SKILLS.setTable();
        //MenuItem.JOURNAL.setTable();
        //MenuItem.OPTIONS.setTable();

        addActor(menuItemHeaderTable);
        addActor(statsTable);

        addActor(MenuItem.INVENTORY.getTable());
    }


    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            MenuItem.next();
            MenuItem.show(MenuItem.current());
        }

        if (MenuItem.current().getTable() != null) {
            MenuItem.current().handleInput(delta);
        }
    }

    public void update(float delta) {
        handleInput(delta);
        act(delta);

        gsm.getBatch().begin();
        gsm.getBatch().draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        gsm.getBatch().draw(statsTable.getBackgroundTexture(), 380, 46);
        if (MenuItem.current().getTable() != null) {
            gsm.getBatch().draw(MenuItem.current().getBackgroundTexture(), 50, 46);
        }
        gsm.getBatch().end();

        MenuItem.updateLabelColors();
    }

}