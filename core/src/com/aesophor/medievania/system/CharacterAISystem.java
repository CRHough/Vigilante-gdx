package com.aesophor.medievania.system;

import com.aesophor.medievania.character.Character;
import com.aesophor.medievania.component.*;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class CharacterAISystem extends IteratingSystem {

    public CharacterAISystem() {
        super(Family.all(CharacterAIComponent.class).get());
    }


    @Override
    public void processEntity(Entity entity, float delta) {
        Character c = (entity instanceof Character) ? (Character) entity : null;
        if (c == null) return;

        StateComponent state = Mappers.STATE.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);
        CharacterStatsComponent stats = Mappers.CHARACTER_STATS.get(entity);
        CombatTargetComponent targets = Mappers.COMBAT_TARGET.get(entity);


        if (state.setToKill) return;

        if (state.alerted && targets.lockedOnTarget != null) {
            // Is the target within melee attack range?
            if (targets.hasInRangeTarget()) {
                // If yes, swing its weapon.
                c.swingWeapon();

                // If the target's heath reaches zero, unset lockedOnTarget and it will stop attacking.
                if (targets.lockedOnTarget.getComponent(StateComponent.class).setToKill) {
                    targets.lockedOnTarget = null;
                }
            } else {
                // If the target isn't within melee attack range, move toward it until it can be attacked.
                B2BodyComponent targetB2Body = Mappers.B2BODY.get(targets.lockedOnTarget);

                float selfPositionX = b2body.body.getPosition().x;
                float targetPositionX = targetB2Body.body.getPosition().x;

                if (Utils.getDistance(selfPositionX, targetPositionX) >= stats.attackRange * 2 / Constants.PPM) {
                    c.getBehavioralModel().moveTowardTarget(targets.lockedOnTarget);

                    // Jump if it gets stucked while moving toward the lockedOnTarget.
                    c.getBehavioralModel().jumpIfStucked(delta, .1f);
                }
            }
        } else {
            c.getBehavioralModel().moveRandomly(delta, 0, 5, 0, 5);
        }
    }

}