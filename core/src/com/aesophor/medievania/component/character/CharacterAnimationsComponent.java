package com.aesophor.medievania.component.character;

import com.aesophor.medievania.component.graphics.AnimationComponent;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class CharacterAnimationsComponent implements Component {

    private final Map<CharacterState, AnimationComponent<TextureRegion>> animations;

    public CharacterAnimationsComponent() {
        animations = new HashMap<>();
    }


    public AnimationComponent<TextureRegion> get(CharacterState state) {
        return animations.get(state);
    }

    public void put(CharacterState state, AnimationComponent<TextureRegion> animation) {
        animations.put(state, animation);
    }

}