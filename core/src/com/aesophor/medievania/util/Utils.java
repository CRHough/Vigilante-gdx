package com.aesophor.medievania.util;

import com.aesophor.medievania.component.character.CharacterDataComponent;
import com.aesophor.medievania.component.graphics.AnimationComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    
    private static Array<TextureRegion> frames = new Array<>();

    private Utils() {
        
    }


    public static Texture getTexture(){

        Pixmap pixmap;
        try {
            pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        }catch (GdxRuntimeException e)
        {
            pixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        }
        pixmap.setColor(Color.BLACK);
        pixmap.drawRectangle(0,0,1,1);

        return new Texture(pixmap);
    }

    /**
     * Calculates the distance between position x1 and x2, always returning positive value.
     * @param x1 position x1.
     * @param x2 position x2.
     * @return positive distance value.
     */
    public static float getDistance(float x1, float x2) {
        float distance = x1 - x2;
        return (distance > 0) ? distance : -distance;
    }
    
    /**
     * Create animations by extracting a set of TextureRegion from the specified Texture atlas.
     * Note that the all sprites must be on the same row.
     * @param atlas source texture atlas.
     * @param characterData target character data component.
     * @param animationName name of the animation (idle/run/jump... etc).
     * @param ppm frame duration scale factor.
     * @return Extracted animations.
     */
    public static AnimationComponent<TextureRegion> createAnimation(TextureAtlas atlas, CharacterDataComponent characterData, String animationName, float ppm) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(animationName);
        CharacterDataComponent.AnimationData animationData = characterData.getAnimationData().get(animationName);

        float frameDuration = animationData.getFrameDuration();
        int firstFrameCount = animationData.getFirstFrameCount();
        int lastFrameCount = animationData.getLastFrameCount();
        int frameWidth = characterData.getFrameWidth();
        int frameHeight = characterData.getFrameHeight();

        frames.clear();
        for (int i = firstFrameCount; i <= lastFrameCount; i++) {
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, frameHeight));
        }
        return new AnimationComponent<>(frameDuration / ppm, frames);
    }


    public static AnimationComponent<TextureRegion> createAnimation(Texture region, float frameDuration,
                                                           int firstFrameCount, int lastFrameCount, int offsetX, int offsetY, int width, int height) {
        frames.clear();
        for (int i = firstFrameCount; i <= lastFrameCount; i++) {
            frames.add(new TextureRegion(region, i * width + offsetX, offsetY, width, height));
        }
        return new AnimationComponent<>(frameDuration, frames);
    }


    /**
     * Generates a random integer between the specified min and max value.
     * @param min minimum value of the integer.
     * @param max maximum value of the integer.
     * @return randomly generated integer.
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Generates a random float between the specified min and max value.
     * @param min minimum value of the float.
     * @param max maximum value of the float.
     * @return randomly generated float.
     */
    public static float randomFloat(float min, float max) {
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }
    
}