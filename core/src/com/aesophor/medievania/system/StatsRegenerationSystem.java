package com.aesophor.medievania.system;

import com.aesophor.medievania.component.CharacterStatsComponent;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.StatsRegenerationComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class StatsRegenerationSystem extends IteratingSystem {

    public StatsRegenerationSystem() {
        super(Family.all(StatsRegenerationComponent.class).get());
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CharacterStatsComponent charStats = Mappers.CHARACTER_STATS.get(entity);
        StatsRegenerationComponent regeneration = Mappers.REGENERATION.get(entity);

        if (regeneration.timer >= 5) {
            if (charStats.health < charStats.fullHealth) {
                if (charStats.health + regeneration.healthRegenRate > charStats.fullHealth) {
                    charStats.health += charStats.fullHealth - charStats.health;
                } else {
                    charStats.health += regeneration.healthRegenRate;
                }
            }

            // Mimic the code above. This is it for today.
            if (charStats.stamina < charStats.fullStamina) {
                charStats.stamina += regeneration.staminaRegenRate;
            }

            if (charStats.magicka < charStats.fullMagicka) {
                charStats.magicka += regeneration.magickaRegenRate;
            }

            regeneration.timer = 0;
        } else {
            regeneration.timer += deltaTime;
        }
    }

}