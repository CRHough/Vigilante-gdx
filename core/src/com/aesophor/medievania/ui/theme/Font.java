package com.aesophor.medievania.ui.theme;

import com.aesophor.medievania.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {

    public static final BitmapFont HEADER;
    public static final BitmapFont REGULAR;

    static {
        // Initialize Header font.
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(GameAssetManager.HEADER_FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetY = 1;
        HEADER = generator.generateFont(parameter);
        generator.dispose();

        // Initialize Regular font.
        generator = new FreeTypeFontGenerator(Gdx.files.internal(GameAssetManager.REGULAR_FONT));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetY = 1;
        REGULAR = generator.generateFont(parameter);
        generator.dispose();
    }

}