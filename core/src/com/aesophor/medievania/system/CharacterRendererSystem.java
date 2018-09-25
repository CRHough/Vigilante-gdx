package com.aesophor.medievania.system;

import com.aesophor.medievania.component.*;
import com.aesophor.medievania.component.character.CharacterDataComponent;
import com.aesophor.medievania.component.character.State;
import com.aesophor.medievania.component.character.StateComponent;
import com.aesophor.medievania.component.character.StatsComponent;
import com.aesophor.medievania.component.character.CharacterAnimationComponent;
import com.aesophor.medievania.component.graphics.SpriteComponent;
import com.aesophor.medievania.component.physics.B2BodyComponent;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class CharacterRendererSystem extends IteratingSystem {

    private Batch batch;
    private Camera camera;
    private World world;

    private CharacterDataComponent characterData;
    private CharacterAnimationComponent animations; // character's animations.
    private SpriteComponent sprite;                 // character's sprite.
    private StateComponent state;                   // character's state.
    private StatsComponent stats;                   // health, magicka, stamina, exp... etc.
    private B2BodyComponent b2body;                 // box2d bodybuilder, body and fixtures.

    public CharacterRendererSystem(Batch batch, Camera camera, World world) {
        super(Family.all(CharacterAnimationComponent.class, StateComponent.class).get());

        this.batch = batch;
        this.camera = camera;
        this.world = world;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.update(delta);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        characterData = Mappers.CHARACTER_DATA.get(entity);
        stats = Mappers.STATS.get(entity);
        b2body = Mappers.B2BODY.get(entity);
        sprite = Mappers.SPRITE.get(entity);
        animations = Mappers.CHARACTER_ANIMATIONS.get(entity);
        state = Mappers.STATE.get(entity);

        if (!state.isKilled()) {
            // If the character's health has reached zero but hasn't die yet,
            // it means that the killedAnimation is not fully played.
            // So here we'll play it until it's finished.
            if (state.isSetToKill()) {
                sprite.setRegion(getFrame(delta));

                // Set killed to true to prevent further rendering updates.
                if (animations.get(State.KILLED).isAnimationFinished(state.getStateTimer())) {
                    world.destroyBody(b2body.getBody());
                    state.setKilled(true);
                }
            } else {
                sprite.setRegion(getFrame(delta));

                // Set attacking back to false, implying the attack has completed.
                if (state.isAttacking() && state.getStateTimer() >= stats.getAttackTime()) {
                    state.setAttacking(false);
                    state.resetTime();
                }
            }

            float textureOffsetX = characterData.getTextureOffsetX();
            float textureOffsetY = characterData.getTextureOffsetY();

            float textureX = b2body.getBody().getPosition().x - sprite.getWidth() / 2 + (textureOffsetX / Constants.PPM);
            float textureY = b2body.getBody().getPosition().y - sprite.getHeight() / 2 + (textureOffsetY / Constants.PPM);
            sprite.setPosition(textureX, textureY);
        }

        sprite.setRegion(getFrame(delta));
        sprite.draw(batch);
    }

    /**
     * Gets the current frame for the character based on its current state and delta time.
     * @param delta delta time.
     * @return the current frame for the character.
     */
    private TextureRegion getFrame(float delta) {
        state.setCurrentState(getState());

        TextureRegion textureRegion;
        switch (state.getCurrentState()) {
            case RUNNING:
                textureRegion = animations.get(State.RUNNING).getKeyFrame(state.getStateTimer(), true);
                break;
            case JUMPING:
                textureRegion = animations.get(State.JUMPING).getKeyFrame(state.getStateTimer(), false);
                break;
            case FALLING:
                textureRegion = animations.get(State.FALLING).getKeyFrame(state.getStateTimer(), false);
                break;
            case CROUCHING:
                textureRegion = animations.get(State.CROUCHING).getKeyFrame(state.getStateTimer(), false);
                break;
            case ATTACKING:
                textureRegion = animations.get(State.ATTACKING).getKeyFrame(state.getStateTimer(), false);
                break;
            case SKILL:
                textureRegion = animations.get(State.SKILL).getKeyFrame(state.getStateTimer(), true);
                break;
            case KILLED:
                textureRegion = animations.get(State.KILLED).getKeyFrame(state.getStateTimer(), false);
                break;
            case IDLE:
            default:
                textureRegion = animations.get(State.IDLE).getKeyFrame(state.getStateTimer(), true);
                break;
        }

        if (!state.isFacingRight() && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            CircleShape shape = (CircleShape) b2body.getMeleeWeaponFixture().getShape();
            shape.setPosition(new Vector2(-stats.getAttackRange() / Constants.PPM, 0));
        } else if (state.isFacingRight() && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            CircleShape shape = (CircleShape) b2body.getMeleeWeaponFixture().getShape();
            shape.setPosition(new Vector2(stats.getAttackRange() / Constants.PPM, 0));
        }

        if (state.getCurrentState() != state.getPreviousState()) {
            state.resetTime();
        } else {
            state.update(delta);
        }

        return textureRegion;
    }

    /**
     * Determines which state the character currently is in.
     * @return character's current state.
     */
    private State getState() {
        if (state.isSetToKill()) {
            return State.KILLED;
        } else if (state.isUsingSkill()) {
            return State.SKILL;
        } else if (state.isAttacking()) {
            return State.ATTACKING;
        } else if (state.isJumping()) {
            return State.JUMPING;
        } else if (b2body.getBody().getLinearVelocity().y < -.5f) {
            return State.FALLING;
        } else if (state.isCrouching()) {
            return State.CROUCHING;
        } else if (b2body.getBody().getLinearVelocity().x > .01f || b2body.getBody().getLinearVelocity().x < -.01f) {
            return State.RUNNING;
        } else {
            return State.IDLE;
        }
    }

}