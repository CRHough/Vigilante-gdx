package com.aesophor.medieval.misc;

import com.aesophor.medieval.Medieval;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

public class Font {
    
    private static final String FONT_FILE = "Font/Vormgevers.ttf";
    private static BitmapFont defaultFont;
    
    static {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        Medieval.manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        Medieval.manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        
        // Next, let's define the params and then load our bigger font
        FreeTypeFontLoaderParameter font = new FreeTypeFontLoaderParameter();
        font.fontFileName = FONT_FILE;
        font.fontParameters.size = 20;
        Medieval.manager.load(FONT_FILE, BitmapFont.class, font);
        Medieval.manager.finishLoading();
        
        defaultFont = Medieval.manager.get(FONT_FILE, BitmapFont.class);
    }
    
    
    public static BitmapFont getDefaultFont() {
        return defaultFont;
    }

}
