package com.aesophor.medievania.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {

    private static final String FONT_FILE = "interface/font/EquipmentPro.ttf";
    private static BitmapFont defaultFont;

    static {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;
        parameter.borderColor = Color.CLEAR;
        parameter.borderWidth = 1.1f;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetY = 1;
        defaultFont = generator.generateFont(parameter);
        defaultFont.getData().setScale(.7f);
        generator.dispose();
    }


    public static BitmapFont getDefaultFont() {
        return defaultFont;
    }

}