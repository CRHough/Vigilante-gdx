package com.aesophor.vigilante.component.character;

import com.aesophor.vigilante.entity.character.Character;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Contains combat targets of a character.
 * com.aesophor.vigilante.system.EnemyAISystem is responsible for handling AI combat behaviors.
 */
public class CombatTargetComponent implements Component {

    private Character lockedOnTarget;
    private final Array<Character> inRangeTargets;

    public CombatTargetComponent() {
        inRangeTargets = new Array<>();
    }


    public boolean hasLockedOnTarget() {
        return lockedOnTarget != null;
    }

    public boolean hasInRangeTarget() {
        return inRangeTargets.size > 0;
    }

    public Character getLockedOnTarget() {
        return lockedOnTarget;
    }

    public void setLockedOnTarget(Character lockedOnTarget) {
        this.lockedOnTarget = lockedOnTarget;
    }

    public void addInRangeTarget(Character target) {
        inRangeTargets.add(target);
    }

    public void removeInRangeTarget(Character target) {
        inRangeTargets.removeValue(target, false);
    }

    public Array<Character> getInRangeTargets() {
        return inRangeTargets;
    }

}