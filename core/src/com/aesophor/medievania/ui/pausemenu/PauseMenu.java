package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseMenu extends Stage {

    private static final String PAUSE_MENU_BACKGROUND = "interface/pause.png";

    private final GameStateManager gsm;
    private final Texture background;
    private final Sound clickSound;

    private final StatsPane statsPane;
    private final MenuDialog menuDialog;

    public PauseMenu(GameStateManager gsm, Player player) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;

        background = gsm.getAssets().get(PAUSE_MENU_BACKGROUND);
        gsm.getAssets().load("sfx/ui/click.wav", Sound.class); // assets should be loaded elsewhere.
        clickSound = gsm.getAssets().get("sfx/ui/click.wav", Sound.class);


        // Initialize header options (Inventory / Equipment / Skills / Quest / Options).
        Table menuItemHeaderTable = new Table();
        menuItemHeaderTable.top().padTop(18f);
        menuItemHeaderTable.defaults().spaceRight(30f);
        menuItemHeaderTable.setFillParent(true);
        MenuPage.buildLabels().forEach(menuItemHeaderTable::add);

        // Initialize stats UI and menu dialog.
        statsPane = new StatsPane(gsm.getAssets(), player);
        menuDialog = new MenuDialog(gsm.getAssets());
        //equipmentSelectionPane = new EquipmentSelectionPane(gsm.getAssets(), player, menuDialog);
        //equipmentSelectionPane.setEquipmentPane(equipmentPane);

        // Initialize pages.
        MenuPage.INVENTORY.addTable(new InventoryTabPane(gsm.getAssets(), player, menuDialog));
        MenuPage.EQUIPMENT.addTable(new EquipmentPane(gsm.getAssets(), player, menuDialog));

        //MenuPage.SKILLS.setTables();
        //MenuPage.JOURNAL.setTables();
        //MenuPage.OPTIONS.setTables();


        addActor(menuItemHeaderTable);
        addActor(statsPane);
        addActor(menuDialog);
        MenuPage.INVENTORY.getTables().forEach(this::addActor);
        MenuPage.EQUIPMENT.getTables().forEach(this::addActor);

        MenuPage.update(MenuPage.current());
    }


    private void handleInput(float delta) {
        // Only process the dialog if it's currently being shown.
        if (menuDialog.isVisible()) {
            menuDialog.handleInput(delta);
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                MenuPage.prev();
                MenuPage.update(MenuPage.current());
                clickSound.play();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                MenuPage.next();
                MenuPage.update(MenuPage.current());
                clickSound.play();
            }

            if (MenuPage.current().getTables().size > 0) {
                MenuPage.current().handleInput(delta);
            }
        }
    }

    public void update(float delta) {
        handleInput(delta);
        act(delta);

        gsm.getBatch().begin();
        gsm.getBatch().draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        gsm.getBatch().draw(statsPane.getBackgroundTexture(), 380, 40);
        if (MenuPage.current().getTables().size > 0 && MenuPage.current().getBackgroundTexture() != null) {
            gsm.getBatch().draw(MenuPage.current().getBackgroundTexture(), 50, 40);
        }
        gsm.getBatch().end();

        MenuPage.updateLabelColors();
    }

}