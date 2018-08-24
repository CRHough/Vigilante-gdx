package com.aesophor.medievania.utils;

import com.aesophor.medievania.Medievania;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

public class Font {
    
    private static final String FONT_FILE = "Interface/Font/Vormgevers.ttf";
    private static BitmapFont defaultFont;
    
    static {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        Medievania.manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        Medievania.manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        
        // Next, let's define the params and then load our bigger font
        FreeTypeFontLoaderParameter font = new FreeTypeFontLoaderParameter();
        font.fontFileName = FONT_FILE;
        font.fontParameters.size = 20;
        Medievania.manager.load(FONT_FILE, BitmapFont.class, font);
        Medievania.manager.finishLoading();
        
        defaultFont = Medievania.manager.get(FONT_FILE, BitmapFont.class);
    }
    
    
    public static BitmapFont getDefaultFont() {
        return defaultFont;
    }

}
