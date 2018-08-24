package com.aesophor.medievania.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Utils {
    
    private static Array<TextureRegion> frames = new Array<TextureRegion>();

    private Utils() {
        
    }
    
    
    /**
     * Create animation by extracting a set of TextureRegion from the specified Texture.
     * Note that the all sprites must be on the same row.
     * @param texture The Texture from which to extract TextureRegion
     * @param frameDuration The time between frames in seconds.
     * @param firstFrameCount The starting frame count of the frames to extract,
     *        usually 0 if correct offsetX is given.
     * @param lastFrameCount The last frame count of the frames to extract.
     * @param offsetX The x offset to apply in order to reach the first frame.
     * @param offsetY The y offset to apply in order to reach the first frame.
     * @param width The width of the TextureRegion. May be negative to flip the sprite when drawn.
     * @param height The height of the TextureRegion. May be negative to flip the sprite when drawn.
     * @return Extracted animation.
     */
    public static Animation<TextureRegion> createAnimation(Texture texture, float frameDuration,
            int firstFrameCount, int lastFrameCount, int offsetX, int offsetY, int width, int height) {
        frames.clear();
        
        for (int i = firstFrameCount; i <= lastFrameCount; i++) {
            frames.add(new TextureRegion(texture, i * width + offsetX, offsetY, width, height));
        }
        
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        frames.clear();
        
        return animation;
    }
    
}