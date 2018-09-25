package com.aesophor.medievania.component.character;

import com.aesophor.medievania.component.graphics.AnimationComponent;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class CharacterAnimationComponent implements Component {

    private final Map<State, AnimationComponent<TextureRegion>> animations;

    public CharacterAnimationComponent() {
        animations = new HashMap<>();
    }


    public AnimationComponent<TextureRegion> get(State state) {
        return animations.get(state);
    }

    public void put(State state, AnimationComponent<TextureRegion> animation) {
        animations.put(state, animation);
    }

}