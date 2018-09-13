package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameStateManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class StatsTable extends Table {

    private static final float ROW_HEIGHT = 6f;

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
        Label.LabelStyle whiteLabelStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);
        Label.LabelStyle greyLabelStyle = new Label.LabelStyle(bitmapFont, Color.GRAY);
        Label.LabelStyle redLabelStyle = new Label.LabelStyle(bitmapFont, Color.FIREBRICK);

        nameLabel = new Label("MARCUS", whiteLabelStyle);
        levelLabel = new Label("Level 10", redLabelStyle);

        healthLabel = new Label("50 / 100", whiteLabelStyle);
        staminaLabel = new Label("100 / 100", whiteLabelStyle);
        magickaLabel = new Label("100 / 100", whiteLabelStyle);

        attackRangeLabel = new Label("100%", whiteLabelStyle);
        attackSpeedLabel = new Label("100%", whiteLabelStyle);
        walkSpeedLabel = new Label("100%", whiteLabelStyle);
        jumpHeightLabel = new Label("100%", whiteLabelStyle);

        strLabel = new Label("7", whiteLabelStyle);
        dexLabel = new Label("12", whiteLabelStyle);
        intLabel = new Label("9", whiteLabelStyle);
        lukLabel = new Label("20", whiteLabelStyle);

        //healthLabel = new Label("", )
        setBounds(380, 50 + 3, statsBackground.getWidth(), statsBackground.getHeight());

        padLeft(8f);


        // Sets the default width of column 0.
        columnDefaults(0).width(80f);
        defaults().height(16f);

        add(nameLabel).left().spaceBottom(3f);
        add(levelLabel).right().spaceBottom(3f).row();

        add(new Label("HEALTH", greyLabelStyle)).left();
        add(healthLabel).right().row();
        add(new Label("STAMINA", greyLabelStyle)).left();
        add(staminaLabel).right().row();
        add(new Label("MAGICKA", greyLabelStyle)).left();
        add(magickaLabel).right().row();

        add(new Label("ATTACK RANGE", greyLabelStyle)).left().padTop(8f);
        add(attackRangeLabel).align(Align.right).padTop(8f).row();
        add(new Label("ATTACK SPEED", greyLabelStyle)).left();
        add(attackSpeedLabel).align(Align.right).row();
        add(new Label("WALK SPEED", greyLabelStyle)).left();
        add(walkSpeedLabel).align(Align.right).row();
        add(new Label("JUMP HEIGHT", greyLabelStyle)).left();
        add(jumpHeightLabel).align(Align.right).row();

        add(new Label("STR", greyLabelStyle)).left().padTop(8f);
        add(strLabel).align(Align.right).padTop(8f).row();
        add(new Label("DEX", greyLabelStyle)).left();
        add(dexLabel).align(Align.right).row();
        add(new Label("INT", greyLabelStyle)).left();
        add(intLabel).align(Align.right).row();
        add(new Label("LUK", greyLabelStyle)).left();
        add(lukLabel).align(Align.right).row();
    }


    public Texture getStatsBackground() {
        return statsBackground;
    }

}