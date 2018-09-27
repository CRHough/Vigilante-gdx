package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.Asset;
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

    // Constants regarding the position and size of each pane.
    private static final float HEADER_PAD_TOP = 18f;
    private static final float HEADER_SPACE_RIGHT = 30f;

    private static final float STATS_PANE_X = 60;
    private static final float STATS_PANE_Y = 40;

    private static final float MENU_DIALOG_X = -65;
    private static final float MENU_DIALOG_Y = 20;

    private static final float INVENTORY_PANE_X = 230;
    private static final float INVENTORY_PANE_Y = 41;
    private static final float INVENTORY_PANE_WIDTH = 270;
    private static final float INVENTORY_PANE_HEIGHT = 120;

    private static final float EQUIPMENT_PANE_X = 230;
    private static final float EQUIPMENT_PANE_Y = 41;


    private final GameStateManager gsm;
    private final Texture background;
    private final Sound clickSound;

    private final StatsPane statsPane;
    private final MenuDialog menuDialog;

    public PauseMenu(GameStateManager gsm, Player player) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;

        background = gsm.getAssets().get(Asset.PAUSE_MENU_BG);
        clickSound = gsm.getAssets().get(Asset.UI_CLICK_SOUND, Sound.class);


        // Initialize header options (Inventory / Equipment / Skills / Quest / Options).
        Table menuItemHeaderTable = new Table();
        menuItemHeaderTable.setFillParent(true);
        menuItemHeaderTable.top().padTop(HEADER_PAD_TOP);
        menuItemHeaderTable.defaults().spaceRight(HEADER_SPACE_RIGHT);
        MenuPage.buildLabels().forEach(menuItemHeaderTable::add);

        // Initialize stats UI and menu dialog.
        statsPane = new StatsPane(gsm.getAssets(), player, STATS_PANE_X, STATS_PANE_Y);
        menuDialog = new MenuDialog(gsm.getAssets(), MENU_DIALOG_X, MENU_DIALOG_Y);

        // Initialize pages.
        MenuPage.INVENTORY.addPane(new InventoryTabPane(gsm.getAssets(), player, menuDialog, INVENTORY_PANE_X, INVENTORY_PANE_Y, INVENTORY_PANE_WIDTH, INVENTORY_PANE_HEIGHT));
        MenuPage.EQUIPMENT.addPane(new EquipmentPane(gsm.getAssets(), player, EQUIPMENT_PANE_X, EQUIPMENT_PANE_Y));
        //MenuPage.SKILLS.addPane();
        //MenuPage.JOURNAL.addPane();
        //MenuPage.OPTIONS.addPane();

        addActor(menuItemHeaderTable);
        addActor(statsPane);
        addActor(menuDialog);
        MenuPage.INVENTORY.getPanes().forEach(this::addActor);
        MenuPage.EQUIPMENT.getPanes().forEach(this::addActor);

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
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                MenuPage.next();
                MenuPage.update(MenuPage.current());
                clickSound.play();
            }

            if (MenuPage.current().getPanes().size > 0) {
                MenuPage.current().handleInput(delta);
            }
        }
    }

    public void update(float delta) {
        handleInput(delta);
        act(delta);

        gsm.getBatch().begin();

        // Render the background of pause menu.
        gsm.getBatch().draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);

        // Render the background of stats pane.
        gsm.getBatch().draw(statsPane.getBackgroundTexture(), statsPane.getX(), statsPane.getY());

        // Render the background texture (if not null) of each pane within this page.
        MenuPage.current().getPanes().forEach(pane -> {
            Texture backgroundTexture = pane.getBackgroundTexture();
            if (backgroundTexture != null) {
                gsm.getBatch().draw(backgroundTexture, pane.getX(), pane.getY());
            }
        });

        gsm.getBatch().end();

        // Highlight only the label of currently selected menu page.
        MenuPage.updateLabelColors();
    }

}