package com.aesophor.vigilante.system;

import com.aesophor.vigilante.component.character.CharacterAIComponent;
import com.aesophor.vigilante.component.character.CombatTargetComponent;
import com.aesophor.vigilante.component.character.CharacterStateComponent;
import com.aesophor.vigilante.component.character.CharacterStatsComponent;
import com.aesophor.vigilante.component.physics.B2BodyComponent;
import com.aesophor.vigilante.entity.character.Character;
import com.aesophor.vigilante.component.*;
import com.aesophor.vigilante.util.Constants;
import com.aesophor.vigilante.util.Utils;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class EnemyAISystem extends IteratingSystem {

    private CharacterStateComponent state;
    private B2BodyComponent b2body;
    private CharacterStatsComponent stats;
    private CombatTargetComponent targets;

    public EnemyAISystem() {
        super(Family.all(CharacterAIComponent.class).get());
    }


    @Override
    public void processEntity(Entity entity, float delta) {
        Character c = (entity instanceof Character) ? (Character) entity : null;
        if (c == null) return;

        state = Mappers.STATE.get(entity);
        b2body = Mappers.B2BODY.get(entity);
        stats = Mappers.STATS.get(entity);
        targets = Mappers.COMBAT_TARGETS.get(entity);


        if (state.isSetToKill()) return;

        if (state.isAlerted() && targets.hasLockedOnTarget() && !Mappers.STATE.get(targets.getLockedOnTarget()).isSetToKill()) {
            // Is the target within melee attack range?
            if (targets.hasInRangeTarget()) {
                // If yes, swing its weapon.
                c.attack();

                // If the locked on target's heath reaches zero, unset lockedOnTarget and it will stop attacking.
                if (targets.getLockedOnTarget().getComponent(CharacterStateComponent.class).isSetToKill()) {
                    if (targets.hasInRangeTarget()) {
                        targets.setLockedOnTarget(targets.getInRangeTargets().first());
                    } else {
                        targets.setLockedOnTarget(null);
                    }
                }
            } else {
                // If the target isn't within melee attack range, move toward it until it can be attacked.
                B2BodyComponent targetB2Body = Mappers.B2BODY.get(targets.getLockedOnTarget());

                float selfPositionX = b2body.getBody().getPosition().x;
                float targetPositionX = targetB2Body.getBody().getPosition().x;

                if (Utils.getDistance(selfPositionX, targetPositionX) >= stats.getAttackRange() * 2 / Constants.PPM) {
                    c.getAIActions().moveTowardTarget(targets.getLockedOnTarget());

                    // Jump if it gets stucked while moving toward the lockedOnTarget.
                    c.getAIActions().jumpIfStucked(delta, .1f);
                }
            }
        } else {
            c.getAIActions().moveRandomly(delta, 0, 5, 0, 5);
        }
    }

}