package com.aesophor.medievania.util;

import com.aesophor.medievania.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {

    private static final String FONT_FILE = "interface/font/m5x7.ttf";
    private static BitmapFont defaultFont;

    private GameStateManager gsm;

    public Font(GameStateManager gsm) {
        this.gsm = gsm;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1.1f;
        //parameter.shadowColor = Color.BLACK;
        //parameter.shadowOffsetY = 1;
        defaultFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public BitmapFont getDefaultFont() {
        return defaultFont;
    }

}