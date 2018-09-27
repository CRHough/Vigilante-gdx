package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameAssetManager;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.StatsComponent;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class StatsPane extends Pane {

    private static final float TITLE_BODY_GAP = 3f;
    private static final float SECTION_GAP = 8f;

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

    public StatsPane(AssetManager assets, Player player, float x, float y) {
        super(assets, player, x, y);

        paneBackgroundTexture = assets.get(GameAssetManager.STATS_BG);

        padLeft(10f).padBottom(4f);
        columnDefaults(0).width(85f);
        defaults().height(16f);

        StatsComponent stats = Mappers.STATS.get(player);
        nameLabel = new Label(stats.getName().toUpperCase(), LabelStyles.WHITE_REGULAR);
        levelLabel = new Label(String.format("Level %d", stats.getLevel()), LabelStyles.RED_REGULAR);

        healthLabel = new Label(String.format("%d / %d", stats.getHealth(), stats.getFullHealth()), LabelStyles.WHITE_REGULAR);
        staminaLabel = new Label(String.format("%d / %d", stats.getStamina(), stats.getFullStamina()), LabelStyles.WHITE_REGULAR);
        magickaLabel = new Label(String.format("%d / %d", stats.getMagicka(), stats.getFullMagicka()), LabelStyles.WHITE_REGULAR);

        attackRangeLabel = new Label(String.format("%2d", stats.getAttackRange()), LabelStyles.WHITE_REGULAR);
        attackSpeedLabel = new Label(String.format("%.2f", stats.getAttackTime()), LabelStyles.WHITE_REGULAR);
        walkSpeedLabel = new Label(String.format("%.2f", stats.getMovementSpeed()), LabelStyles.WHITE_REGULAR);
        jumpHeightLabel = new Label(String.format("%.2f", stats.getJumpHeight()), LabelStyles.WHITE_REGULAR);

        strLabel = new Label(String.format("%d", stats.getStr()), LabelStyles.WHITE_REGULAR);
        dexLabel = new Label(String.format("%d", stats.getStr()), LabelStyles.WHITE_REGULAR);
        intLabel = new Label(String.format("%d", stats.getStr()), LabelStyles.WHITE_REGULAR);
        lukLabel = new Label(String.format("%d", stats.getStr()), LabelStyles.WHITE_REGULAR);


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

}