package com.aesophor.medievania.system;

import com.aesophor.medievania.component.character.CharacterStatsComponent;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.CharacterStatsRegenComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class StatsRegenerationSystem extends IteratingSystem {

    private CharacterStatsComponent charStats;
    private CharacterStatsRegenComponent regeneration;

    public StatsRegenerationSystem() {
        super(Family.all(CharacterStatsRegenComponent.class).get());
    }


    @Override
    protected void processEntity(Entity entity, float delta) {
        charStats = Mappers.STATS.get(entity);
        regeneration = Mappers.REGENERATION.get(entity);

        if (regeneration.hasElapsed()) {
            if (!charStats.isHealthFull()) {
                charStats.modHealth(charStats.getNextHealthRegen(regeneration.getHealthRegenRate()));
            }

            if (!charStats.isStaminaFull()) {
                charStats.modStamina(charStats.getNextStaminaRegen(regeneration.getStaminaRegenRate()));
            }

            if (!charStats.isMagickaFull()) {
                charStats.modMagicka(charStats.getNextMagickaRegen(regeneration.getMagickaRegenRate()));
            }

            regeneration.resetTimer();
        } else {
            regeneration.update(delta);
        }
    }

}