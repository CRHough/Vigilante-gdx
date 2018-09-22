package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;

public class CharacterDataComponent implements Component {

    private String atlas;
    private int textureOffsetX;
    private int textureOffsetY;

    private StatsComponent stats;
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

    public StatsComponent getStats() {
        return stats;
    }

    public HashMap<String, Float> getItems() {
        return items;
    }

}