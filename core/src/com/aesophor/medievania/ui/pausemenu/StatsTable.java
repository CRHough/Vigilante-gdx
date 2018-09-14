package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class StatsTable extends Table {

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

    public StatsTable(GameStateManager gsm) {
        statsBackground = gsm.getAssets().get("interface/stats_bg.png");

        //top().right().padRight(75f).padTop(35f);
        //setPosition(420f, 350f);
        bottom().left();
        //setPosition(420f, 262f);
        setFillParent(true);

        BitmapFont bitmapFont = gsm.getFont().getDefaultFont();


        nameLabel = new Label("MARCUS", LabelStyles.WHITE);
        levelLabel = new Label("Level 10", LabelStyles.RED);

        healthLabel = new Label("50 / 100", LabelStyles.WHITE);
        staminaLabel = new Label("100 / 100", LabelStyles.WHITE);
        magickaLabel = new Label("100 / 100", LabelStyles.WHITE);

        attackRangeLabel = new Label("100%", LabelStyles.WHITE);
        attackSpeedLabel = new Label("100%", LabelStyles.WHITE);
        walkSpeedLabel = new Label("100%", LabelStyles.WHITE);
        jumpHeightLabel = new Label("100%", LabelStyles.WHITE);

        strLabel = new Label("7", LabelStyles.WHITE);
        dexLabel = new Label("12", LabelStyles.WHITE);
        intLabel = new Label("9", LabelStyles.WHITE);
        lukLabel = new Label("20", LabelStyles.WHITE);

        //healthLabel = new Label("", )
        setBounds(380, 46 + 3, statsBackground.getWidth(), statsBackground.getHeight());

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


    public Texture getBackgroundTexture() {
        return statsBackground;
    }

}