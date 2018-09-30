package com.aesophor.medievania.system.graphics;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.*;
import com.aesophor.medievania.component.graphics.SpriteComponent;
import com.aesophor.medievania.component.physics.B2BodyComponent;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

/**
 * BodyRendererSystem is responsible for rendering character bodies based on the states of characters,
 * and it will determine which state should a character should be in at any moment.
 */
public class BodyRendererSystem extends CharacterRendererSystem {

    public BodyRendererSystem(Batch batch, Camera camera, World world) {
        super(batch, camera, world);
    }


    @Override
    protected void processEntity(Entity entity, float delta) {
        CharacterDataComponent characterData = Mappers.CHARACTER_DATA.get(entity);
        StatsComponent stats = Mappers.STATS.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);
        SpriteComponent sprite = Mappers.SPRITE.get(entity);
        CharacterAnimationComponent bodyAnimations = Mappers.CHARACTER_ANIMATIONS.get(entity);
        StateComponent state = Mappers.STATE.get(entity);


        // Fetch and set the latest state of the character.
        state.setCurrentState(getState(entity));

        // Immediately update the state timer before using it to get the next frame,
        // otherwise the animation would seem laggy, because it would fetch an outdated and incorrect one!
        float stateTimer = state.getStateTimer();
        state.setStateTimer((state.getCurrentState() == state.getPreviousState()) ? stateTimer + delta : 0);


        sprite.setRegion(getFrame(entity, bodyAnimations, delta));

        if (!state.isKilled()) {
            // If the character's health has reached zero but hasn't die yet,
            // it means that the killedAnimation is not fully played.
            // So here we'll play it until it's finished.
            if (state.isSetToKill()) {
                // Set killed to true to prevent further rendering updates.
                if (bodyAnimations.get(State.KILLED).isAnimationFinished(state.getStateTimer())) {
                    world.destroyBody(b2body.getBody());
                    state.setKilled(true);
                }
            } else {
                // Set attacking back to false, implying the attack has completed.
                if (state.isAttacking() && state.getStateTimer() >= stats.getAttackTime()) {
                    state.setAttacking(false);
                    state.resetStateTimer();
                }

                if (state.isSheathing() && state.getStateTimer() >= .8f) {
                    state.setSheathing(false);
                    state.setSheathed(true);
                }

                if (state.isUnsheathing() && state.getStateTimer() >= .8f) {
                    state.setUnsheathing(false);
                    state.setSheathed(false);
                }
            }

            float textureOffsetX = characterData.getTextureOffsetX();
            float textureOffsetY = characterData.getTextureOffsetY();

            float textureX = b2body.getBody().getPosition().x - sprite.getWidth() / 2 + (textureOffsetX / Constants.PPM);
            float textureY = b2body.getBody().getPosition().y - sprite.getHeight() / 2 + (textureOffsetY / Constants.PPM);
            sprite.setPosition(textureX, textureY);
        }

        sprite.draw(batch);
    }

}