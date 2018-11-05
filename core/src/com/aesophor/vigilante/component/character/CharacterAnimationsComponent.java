package com.aesophor.vigilante.component.character;

import com.aesophor.vigilante.component.graphics.AnimationComponent;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains character animations for all character states.
 * The animation can either be character's body animation, or be the equipment animation
 * which will be rendered atop the body.
 *
 * The system responsible for rendering body animation is com.aesophor.vigilante.system.graphics.BodyRendererSystem,
 * while the equipment animation is handled by com.aesophor.vigilante.system.graphics.EquipmentRendererSystem.
 */
public class CharacterAnimationsComponent implements Component {

    private final Map<CharacterState, AnimationComponent<TextureRegion>> animations;

    public CharacterAnimationsComponent() {
        animations = new HashMap<>();
    }


    /**
     * Gets the animation of the specified character state.
     * @param state target state.
     * @return animation of that state.
     */
    public AnimationComponent<TextureRegion> get(CharacterState state) {
        return animations.get(state);
    }

    /**
     * Puts the specified character state and the associating animation into the hash map.
     * @param state character state.
     * @param animation animation which belongs to the given character state.
     */
    public void put(CharacterState state, AnimationComponent<TextureRegion> animation) {
        animations.put(state, animation);
    }

}