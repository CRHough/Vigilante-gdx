package com.aesophor.vigilante.event.combat;

import com.aesophor.vigilante.entity.character.Character;
import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class InflictDamageEvent extends GameEvent {

    private final Character source;
    private final Character target;
    private final int damage;

    public InflictDamageEvent(Character source, Character target, int damage) {
        super(GameEventType.INFLICT_DAMAGE);
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