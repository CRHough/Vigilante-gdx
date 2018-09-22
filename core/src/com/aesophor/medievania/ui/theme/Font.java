package com.aesophor.medievania.ui.theme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {

    private static final String HEADER_FONT = "interface/font/MatchupPro.ttf";
    private static final String REGULAR_FONT = "interface/font/HeartbitXX.ttf";

    public static final BitmapFont HEADER;
    public static final BitmapFont REGULAR;

    static {
        // Initialize Header font.
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(HEADER_FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetY = 1;
        HEADER = generator.generateFont(parameter);
        generator.dispose();

        // Initialize Regular font.
        generator = new FreeTypeFontGenerator(Gdx.files.internal(REGULAR_FONT));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetY = 1;
        REGULAR = generator.generateFont(parameter);
        generator.dispose();
    }

}