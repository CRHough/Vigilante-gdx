package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;

public class CharacterDataComponent implements Component {

    public static class FrameData {
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
    private HashMap<String, FrameData> frameData;
    private HashMap<String, String> soundData;

    private StatsComponent stats;
    private StatsRegenerationComponent statsRegen;
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

    public HashMap<String, FrameData> getFrameData() {
        return frameData;
    }

    public HashMap<String, String> getSoundData() {
        return soundData;
    }

    public StatsComponent getStats() {
        return stats;
    }

    public StatsRegenerationComponent getStatsRegen() {
        return statsRegen;
    }

    public HashMap<String, Float> getItems() {
        return items;
    }

}