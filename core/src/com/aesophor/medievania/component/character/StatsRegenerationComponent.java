package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;

public class StatsRegenerationComponent implements Component {

    private static final float REGEN_INTERVAL = 3;

    private int healthRegenRate;  // regen x points of health every n secs.
    private int staminaRegenRate; // regen y points of stamina every n secs.
    private int magickaRegenRate; // regen z points of magicka every n secs.
    private float timer;

    public StatsRegenerationComponent() {

    }

    public StatsRegenerationComponent(StatsRegenerationComponent statsRegen) {
        this.healthRegenRate = statsRegen.getHealthRegenRate();
        this.staminaRegenRate = statsRegen.getStaminaRegenRate();
        this.magickaRegenRate = statsRegen.getMagickaRegenRate();
    }


    public void update(float delta) {
        timer += delta;
    }

    public boolean hasElapsed() {
        return timer >= REGEN_INTERVAL;
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