package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.StatsComponent;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.ui.LabelStyles;
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
        setPosition(-statsBackground.getWidth() / 2 - 8, -(300 - statsBackground.getHeight()) / 2 + 5);
        setFillParent(true);

        StatsComponent stats = Mappers.STATS.get(player);


        nameLabel = new Label(stats.getName().toUpperCase(), LabelStyles.WHITE_REGULAR);
        levelLabel = new Label(String.format("Level %d", stats.getLevel()), LabelStyles.RED_REGULAR);

        healthLabel = new Label(String.format("%d / %d", stats.getHealth(), stats.getFullHealth()), LabelStyles.WHITE_REGULAR);
        staminaLabel = new Label(String.format("%d / %d", stats.getStamina(), stats.getFullStamina()), LabelStyles.WHITE_REGULAR);
        magickaLabel = new Label(String.format("%d / %d", stats.getMagicka(), stats.getFullMagicka()), LabelStyles.WHITE_REGULAR);

        attackRangeLabel = new Label("100%", LabelStyles.WHITE_REGULAR);
        attackSpeedLabel = new Label("100%", LabelStyles.WHITE_REGULAR);
        walkSpeedLabel = new Label("100%", LabelStyles.WHITE_REGULAR);
        jumpHeightLabel = new Label("100%", LabelStyles.WHITE_REGULAR);

        strLabel = new Label("7", LabelStyles.WHITE_REGULAR);
        dexLabel = new Label("12", LabelStyles.WHITE_REGULAR);
        intLabel = new Label("9", LabelStyles.WHITE_REGULAR);
        lukLabel = new Label("20", LabelStyles.WHITE_REGULAR);

        padLeft(8f);


        // Sets the default width of column 0.
        columnDefaults(0).width(80f);
        defaults().height(16f);

        add(nameLabel).left().spaceBottom(TITLE_BODY_GAP);
        add(levelLabel).right().spaceBottom(TITLE_BODY_GAP).row();

        add(new Label("HEALTH", LabelStyles.GRAY_REGULAR)).left();
        add(healthLabel).right().row();
        add(new Label("STAMINA", LabelStyles.GRAY_REGULAR)).left();
        add(staminaLabel).right().row();
        add(new Label("MAGICKA", LabelStyles.GRAY_REGULAR)).left();
        add(magickaLabel).right().row();

        add(new Label("ATTACK RANGE", LabelStyles.GRAY_REGULAR)).left().padTop(SECTION_GAP);
        add(attackRangeLabel).align(Align.right).padTop(SECTION_GAP).row();
        add(new Label("ATTACK SPEED", LabelStyles.GRAY_REGULAR)).left();
        add(attackSpeedLabel).align(Align.right).row();
        add(new Label("WALK SPEED", LabelStyles.GRAY_REGULAR)).left();
        add(walkSpeedLabel).align(Align.right).row();
        add(new Label("JUMP HEIGHT", LabelStyles.GRAY_REGULAR)).left();
        add(jumpHeightLabel).align(Align.right).row();

        add(new Label("STR", LabelStyles.GRAY_REGULAR)).left().padTop(SECTION_GAP);
        add(strLabel).align(Align.right).padTop(SECTION_GAP).row();
        add(new Label("DEX", LabelStyles.GRAY_REGULAR)).left();
        add(dexLabel).align(Align.right).row();
        add(new Label("INT", LabelStyles.GRAY_REGULAR)).left();
        add(intLabel).align(Align.right).row();
        add(new Label("LUK", LabelStyles.GRAY_REGULAR)).left();
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