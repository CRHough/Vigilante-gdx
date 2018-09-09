package com.aesophor.medievania.system;

import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.component.*;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class EnemyAISystem extends IteratingSystem {

    private StateComponent state;
    private B2BodyComponent b2body;
    private CharacterStatsComponent stats;
    private CombatTargetComponent targets;

    public EnemyAISystem() {
        super(Family.all(EnemyAIComponent.class).get());
    }


    @Override
    public void processEntity(Entity entity, float delta) {
        Character c = (entity instanceof Character) ? (Character) entity : null;
        if (c == null) return;

        state = Mappers.STATE.get(entity);
        b2body = Mappers.B2BODY.get(entity);
        stats = Mappers.CHARACTER_STATS.get(entity);
        targets = Mappers.COMBAT_TARGET.get(entity);


        if (state.setToKill) return;

        if (state.alerted && targets.hasLockedOnTarget()) {
            // Is the target within melee attack range?
            if (targets.hasInRangeTarget()) {
                // If yes, swing its weapon.
                c.swingWeapon();

                // If the target's heath reaches zero, unset lockedOnTarget and it will stop attacking.
                if (targets.lockedOnTarget.getComponent(StateComponent.class).setToKill) {
                    if (targets.hasInRangeTarget()) {
                        targets.lockedOnTarget = targets.inRangeTargets.first();
                    } else {
                        targets.lockedOnTarget = null;
                    }
                }
            } else {
                // If the target isn't within melee attack range, move toward it until it can be attacked.
                B2BodyComponent targetB2Body = Mappers.B2BODY.get(targets.lockedOnTarget);

                float selfPositionX = b2body.body.getPosition().x;
                float targetPositionX = targetB2Body.body.getPosition().x;

                if (Utils.getDistance(selfPositionX, targetPositionX) >= stats.attackRange * 2 / Constants.PPM) {
                    c.getAIActions().moveTowardTarget(targets.lockedOnTarget);

                    // Jump if it gets stucked while moving toward the lockedOnTarget.
                    c.getAIActions().jumpIfStucked(delta, .1f);
                }
            }
        } else {
            c.getAIActions().moveRandomly(delta, 0, 5, 0, 5);
        }
    }

}