package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.StatsComponent;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.ui.LabelStyles;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class StatsTable extends Table implements MenuItemTable {

    private static final float TITLE_BODY_GAP = 3f;
    private static final float SECTION_GAP = 8f;

    private final Texture statsBackground;

    private Label nameLabel;
    private Label levelLabel;

    private Label healthLabel;
    private Label staminaLabel;
    private Label magickaLabel;

    private Label rhWeaponLabel;
    private Label lhWeaponLabel;
    private Label armorRatingLabel;

    private Label attackRangeLabel;
    private Label attackSpeedLabel;
    private Label walkSpeedLabel;
    private Label jumpHeightLabel;

    private Label strLabel;
    private Label dexLabel;
    private Label intLabel;
    private Label lukLabel;

    public StatsTable(GameStateManager gsm, Player player) {
        statsBackground = gsm.getAssets().get("interface/stats_bg.png");

        top().right();
        setPosition(-statsBackground.getWidth() / 2 - 5, -(300 - statsBackground.getHeight()) / 2 + 5);
        setFillParent(true);

        StatsComponent stats = Mappers.CHARACTER_STATS.get(player);


        nameLabel = new Label(stats.getName().toUpperCase(), LabelStyles.WHITE);
        levelLabel = new Label(String.format("Level %d", stats.getLevel()), LabelStyles.RED);

        healthLabel = new Label(String.format("%d / %d", stats.getHealth(), stats.getFullHealth()), LabelStyles.WHITE);
        staminaLabel = new Label(String.format("%d / %d", stats.getStamina(), stats.getFullStamina()), LabelStyles.WHITE);
        magickaLabel = new Label(String.format("%d / %d", stats.getMagicka(), stats.getFullMagicka()), LabelStyles.WHITE);

        attackRangeLabel = new Label("100%", LabelStyles.WHITE);
        attackSpeedLabel = new Label("100%", LabelStyles.WHITE);
        walkSpeedLabel = new Label("100%", LabelStyles.WHITE);
        jumpHeightLabel = new Label("100%", LabelStyles.WHITE);

        strLabel = new Label("7", LabelStyles.WHITE);
        dexLabel = new Label("12", LabelStyles.WHITE);
        intLabel = new Label("9", LabelStyles.WHITE);
        lukLabel = new Label("20", LabelStyles.WHITE);

        padLeft(8f);


        // Sets the default width of column 0.
        columnDefaults(0).width(80f);
        defaults().height(16f);

        add(nameLabel).left().spaceBottom(TITLE_BODY_GAP);
        add(levelLabel).right().spaceBottom(TITLE_BODY_GAP).row();

        add(new Label("HEALTH", LabelStyles.GRAY)).left();
        add(healthLabel).right().row();
        add(new Label("STAMINA", LabelStyles.GRAY)).left();
        add(staminaLabel).right().row();
        add(new Label("MAGICKA", LabelStyles.GRAY)).left();
        add(magickaLabel).right().row();

        add(new Label("ATTACK RANGE", LabelStyles.GRAY)).left().padTop(SECTION_GAP);
        add(attackRangeLabel).align(Align.right).padTop(SECTION_GAP).row();
        add(new Label("ATTACK SPEED", LabelStyles.GRAY)).left();
        add(attackSpeedLabel).align(Align.right).row();
        add(new Label("WALK SPEED", LabelStyles.GRAY)).left();
        add(walkSpeedLabel).align(Align.right).row();
        add(new Label("JUMP HEIGHT", LabelStyles.GRAY)).left();
        add(jumpHeightLabel).align(Align.right).row();

        add(new Label("STR", LabelStyles.GRAY)).left().padTop(SECTION_GAP);
        add(strLabel).align(Align.right).padTop(SECTION_GAP).row();
        add(new Label("DEX", LabelStyles.GRAY)).left();
        add(dexLabel).align(Align.right).row();
        add(new Label("INT", LabelStyles.GRAY)).left();
        add(intLabel).align(Align.right).row();
        add(new Label("LUK", LabelStyles.GRAY)).left();
        add(lukLabel).align(Align.right).row();
    }


    @Override
    public void handleInput(float delta) {

    }

    @Override
    public Texture getBackgroundTexture() {
        return statsBackground;
    }

}