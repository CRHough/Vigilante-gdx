package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;

public class StatsRegenerationComponent implements Component {

    private final float regenInterval;
    private int healthRegenRate;  // regen x points of health every n secs.
    private int staminaRegenRate; // regen y points of stamina every n secs.
    private int magickaRegenRate; // regen z points of magicka every n secs.
    private float timer;

    public StatsRegenerationComponent(float regenInterval, int healthRegenRate, int staminaRegenRate, int magickaRegenRate) {
        this.regenInterval = regenInterval;
        this.healthRegenRate = healthRegenRate;
        this.staminaRegenRate = staminaRegenRate;
        this.magickaRegenRate = magickaRegenRate;
    }


    public void update(float delta) {
        timer += delta;
    }

    public boolean hasElapsed() {
        return timer >= regenInterval;
    }

    public void resetTimer() {
        timer = 0;
    }


    public int getHealthRegenRate() {
        return healthRegenRate;
    }

    public int getStaminaRegenRate() {
        return staminaRegenRate;
    }

    public int getMagickaRegenRate() {
        return magickaRegenRate;
    }


    public void setHealthRegenRate(int healthRegenRate) {
        this.healthRegenRate = healthRegenRate;
    }

    public void setStaminaRegenRate(int staminaRegenRate) {
        this.staminaRegenRate = staminaRegenRate;
    }

    public void setMagickaRegenRate(int magickaRegenRate) {
        this.magickaRegenRate = magickaRegenRate;
    }

}