package com.aesophor.medievania.component.character;

import com.aesophor.medievania.component.skill.SkillType;
import com.aesophor.medievania.entity.skill.Skill;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SkillComponent implements Component {

    private final Map<SkillType, Array<Skill>> skills;

    public SkillComponent() {
        skills = new HashMap<>();

        Arrays.stream(SkillType.values()).forEach(skillType -> {
            skills.put(skillType, new Array<>());
        });
    }


    public Array<Skill> get(SkillType skillType) {
        return skills.get(skillType);
    }

    public void add(Skill skill) {
        skills.get(skill.getType()).add(skill);
        // Fire new skill added event.
    }

    public void remove(Skill skill) {
        skills.get(skill.getType()).removeValue(skill, false);
        // Fire skill removed event.
    }

    @Override
    public String toString() {
        return skills.toString();
    }

}