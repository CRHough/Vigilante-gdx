package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;

import java.util.HashMap;

public class CharacterDataComponent implements Component {

    public static class AnimationData {
        private float frameDuration;
        private int frameStartCount;
        private int frameEndCount;

        public float getFrameDuration() {
            return frameDuration;
        }
        public int getFrameStartCount() {
            return frameStartCount;
        }
        public int getFrameEndCount() {
            return frameEndCount;
        }
    }

    private String atlas;
    private int textureOffsetX;
    private int textureOffsetY;
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

    public int getTextureOffsetX() {
        return textureOffsetX;
    }

    public int getTextureOffsetY() {
        return textureOffsetY;
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