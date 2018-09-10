package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class AnimationComponent implements Component {

    private final Map<State, Animation<TextureRegion>> animations;

    public AnimationComponent() {
        animations = new HashMap<>();
    }


    public Animation<TextureRegion> get(State state) {
        return animations.get(state);
    }

    public void put(State state, Animation<TextureRegion> animation) {
        animations.put(state, animation);
    }

}