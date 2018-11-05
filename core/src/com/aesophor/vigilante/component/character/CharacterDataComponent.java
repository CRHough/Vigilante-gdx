package com.aesophor.vigilante.component.character;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;

/**
 * A data container to load all data for a character from character.json.
 * It contains animation data (each frame's duration, frame positions in texture atlas...etc),
 * character texture atlas, sounds, stats, regen, and the items it can drop.
 */
public class CharacterDataComponent implements Component {

    static public class AnimationData {
        private float frameDuration;
        private int firstFrameCount;
        private int lastFrameCount;

        public float getFrameDuration() {
            return frameDuration;
        }
        public int getFirstFrameCount() {
            return firstFrameCount;
        }
        public int getLastFrameCount() {
            return lastFrameCount;
        }
    }

    private String atlas;
    private int spriteOffsetX;
    private int spriteOffsetY;
    private float spriteScaleX;
    private float spriteScaleY;
    private int frameWidth;
    private int frameHeight;
    private HashMap<String, AnimationData> animationData;
    private HashMap<String, String> soundData;

    private CharacterStatsComponent stats;
    private CharacterStatsRegenComponent statsRegen;
    private HashMap<String, Float> items;

    public String getAtlas() {
        return atlas;
    }

    public int getSpriteOffsetX() {
        return spriteOffsetX;
    }

    public int getSpriteOffsetY() {
        return spriteOffsetY;
    }

    public float getSpriteScaleX() {
        return spriteScaleX;
    }

    public float getSpriteScaleY() {
        return spriteScaleY;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public HashMap<String, AnimationData> getAnimationData() {
        return animationData;
    }

    public HashMap<String, String> getSoundData() {
        return soundData;
    }

    public CharacterStatsComponent getStats() {
        return stats;
    }

    public CharacterStatsRegenComponent getStatsRegen() {
        return statsRegen;
    }

    public HashMap<String, Float> getItems() {
        return items;
    }

}