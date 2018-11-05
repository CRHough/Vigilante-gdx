package com.aesophor.vigilante.event.combat;

import com.aesophor.vigilante.entity.character.Character;
import com.aesophor.vigilante.event.GameEvent;
import com.aesophor.vigilante.event.GameEventType;

public class CharacterKilledEvent extends GameEvent {

    private final Character killer;
    private final Character deceased;

    public CharacterKilledEvent(Character killer, Character deceased) {
        super(GameEventType.CHARACTER_KILLED);

        this.killer = killer;
        this.deceased = deceased;
    }


    public Character getKiller() {
        return killer;
    }

    public Character getDeceased() {
        return deceased;
    }

}