package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

public class AnimationComponent implements Component {

    public HashMap<StateComponent.State, Animation<TextureRegion>> animations;

    public AnimationComponent() {
        animations = new HashMap<>();
    }

}