package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseMenu extends Stage {

    private final GameStateManager gsm;
    private Player player;

    private Texture background;

    private Table table;
    private StatsTable statsTable;

    public PauseMenu(GameStateManager gsm) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;

        background = gsm.getAssets().get("interface/pause.png");



        table = new Table();
        table.center().top().padTop(26f);
        table.setFillParent(true);
        // table.add(new Label("PAUSED", new Label.LabelStyle(gsm.getFont().getDefaultFont(), Color.WHITE)));

        statsTable = new StatsTable(gsm);

        addActor(table);
        addActor(statsTable);
    }


    public void update(float delta) {
        act(delta);

        //gsm.clearScreen();
        gsm.getBatch().begin();
        gsm.getBatch().draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        gsm.getBatch().draw(statsTable.getStatsBackground(), 380, 50);
        gsm.getBatch().end();

        /*
        inventoryTable.clearChildren();

        InventoryComponent inventory = Mappers.INVENTORY.get(player);

        for (Item item : inventory.get(ItemType.EQUIPMENT)) {
            inventoryTable.add(new Label(item.toString(), new Label.LabelStyle(gsm.getFont().getDefaultFont(), Color.WHITE)));
            inventoryTable.row();
        }
        */
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}