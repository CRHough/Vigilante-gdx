package com.aesophor.vigilante.ui.component;

import com.aesophor.vigilante.entity.character.Character;

public class DamageIndicator extends TimedLabel {

    private Character character;

    public DamageIndicator(CharSequence text, LabelStyle style, float lifetime, Character character) {
        super(text, style, lifetime);
        this.character = character;
    }


    public Character getCharacter() {
        return character;
    }

}