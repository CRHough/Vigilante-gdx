package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;

public class EnemyDataComponent implements Component {

    private String image;
    private StatsComponent stats;
    private HashMap<String, Float> items;

    public String getImage() {
        return image;
    }

    public StatsComponent getStats() {
        return stats;
    }

    public HashMap<String, Float> getItems() {
        return items;
    }

}