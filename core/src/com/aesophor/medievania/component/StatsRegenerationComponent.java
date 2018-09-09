package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;

public class StatsRegenerationComponent implements Component {

    public int healthRegenRate;  // regen x points of health every 5 secs.
    public int staminaRegenRate; // regen y points of stamina every 5 secs.
    public int magickaRegenRate; // regen z points of magicka every 5 secs.
    public float timer;

    public StatsRegenerationComponent(int healthRegenRate, int staminaRegenRate, int magickaRegenRate) {
        this.healthRegenRate = healthRegenRate;
        this.staminaRegenRate = staminaRegenRate;
        this.magickaRegenRate = magickaRegenRate;
    }

}