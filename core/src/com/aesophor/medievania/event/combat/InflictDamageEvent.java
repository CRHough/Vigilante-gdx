package com.aesophor.medievania.event.combat;

import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class InflictDamageEvent extends GameEvent {

    private final Character source;
    private final Character target;
    private final int damage;

    public InflictDamageEvent(Character source, Character target, int damage) {
        super(GameEventType.HEALTH_CHANGED);
        this.source = source;
        this.target = target;
        this.damage = damage;
    }


    public Character getSource() {
        return source;
    }

    public Character getTarget() {
        return target;
    }

    public int getDamage() {
        return damage;
    }

}