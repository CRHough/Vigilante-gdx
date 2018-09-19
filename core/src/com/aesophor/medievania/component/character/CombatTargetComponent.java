package com.aesophor.medievania.component.character;

import com.aesophor.medievania.entity.character.Character;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

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

    public Array<Character> getInRangeTargets() {
        return inRangeTargets;
    }

}