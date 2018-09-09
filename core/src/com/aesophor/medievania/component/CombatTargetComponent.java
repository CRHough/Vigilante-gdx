package com.aesophor.medievania.component;

import com.aesophor.medievania.entity.character.Character;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class CombatTargetComponent implements Component {

    public Character lockedOnTarget;
    public Array<Character> inRangeTargets;

    public CombatTargetComponent() {
        inRangeTargets = new Array<>();
    }


    public boolean hasLockedOnTarget() {
        return lockedOnTarget != null;
    }

    public boolean hasInRangeTarget() {
        return inRangeTargets.size > 0;
    }

}
