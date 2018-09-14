package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private Label[] itemLabels;
    private String[] menuItems;
    private int currentItem;

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

        menuItems = new String[] {"INVENTORY", "EQUIPMENT", "SKILLS", "JOURNAL", "OPTIONS"};
        itemLabels = new Label[menuItems.length];

        for (int i = 0; i < menuItems.length; i++) {
            itemLabels[i] = new Label(menuItems[i], new Label.LabelStyle(gsm.getFont().getDefaultFont(), Color.WHITE));
            optionsTable.add(itemLabels[i]);
        }

        statsTable = new StatsTable(gsm); // pass player into StatsTable.
        inventoryTable = new InventoryTable(gsm);

        addActor(optionsTable);
        addActor(statsTable);
        addActor(inventoryTable);
    }


    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            currentItem++;
            if (currentItem > itemLabels.length - 1) currentItem = 0;
            show(currentItem);
        }
    }

    public void update(float delta) {

        act(delta);

        //gsm.clearScreen();
        gsm.getBatch().begin();
        handleInput(delta);
        gsm.getBatch().draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        gsm.getBatch().draw(statsTable.getBackgroundTexture(), 380, 46);
        if (currentItem == 0) gsm.getBatch().draw(inventoryTable.getBackgroundTexture(), 50, 46);
        gsm.getBatch().end();

        /*
        inventoryTable.clearChildren();

        InventoryComponent inventory = Mappers.INVENTORY.get(player);

        for (Item item : inventory.get(ItemType.EQUIPMENT)) {
            inventoryTable.add(new Label(item.toString(), new Label.LabelStyle(gsm.getFont().getDefaultFont(), Color.WHITE)));
            inventoryTable.row();
        }
        */

        for (int i = 0; i < itemLabels.length; i++) {
            if (i == currentItem) itemLabels[i].setColor(Color.WHITE);
            else itemLabels[i].setColor(Color.GRAY);
        }
    }

    private void show(int currentItem) {
        switch (currentItem) {

            case 0: // Inventory
                inventoryTable.setVisible(true);
                break;

            case 1: // Equipment
                inventoryTable.setVisible(false);
                break;

            case 2: // Skills
                break;

            case 3: // Journal
                break;

            case 4: // Options
                break;

            default:
                break;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}