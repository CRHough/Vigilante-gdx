package com.aesophor.medievania.component;

import com.aesophor.medievania.character.Character;
import com.badlogic.ashley.core.Component;

public class CombatTargetComponent implements Component {

    public Character lockedOnTarget;
    public Character inRangeTarget;

    public boolean hasLockedOnTarget() {
        return lockedOnTarget != null;
    }

    public boolean hasInRangeTarget() {
        return inRangeTarget != null;
    }

}
