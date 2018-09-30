package com.aesophor.medievania.system.graphics;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.CharacterAnimationComponent;
import com.aesophor.medievania.component.character.State;
import com.aesophor.medievania.component.character.StateComponent;
import com.aesophor.medievania.component.character.StatsComponent;
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

public abstract class CharacterRendererSystem extends IteratingSystem {

    protected final Batch batch;
    protected final Camera camera;
    protected final World world;

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


    /**
     * Gets the current frame for the character based on its current state and delta time.
     * @param entity character entity.
     * @param animations animation component from either character's body or character's equipped equipment.
     * @param delta delta time.
     * @return the current frame for the character.
     */
    protected TextureRegion getFrame(Entity entity, CharacterAnimationComponent animations, float delta) {
        StateComponent state = Mappers.STATE.get(entity);
        StatsComponent stats = Mappers.STATS.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);

        TextureRegion textureRegion;
        switch (state.getCurrentState()) {
            case RUNNING_UNSHEATHED:
                textureRegion = animations.get(State.RUNNING_UNSHEATHED).getKeyFrame(state.getStateTimer(), true);
                break;
            case RUNNING_SHEATHED:
                textureRegion = animations.get(State.RUNNING_SHEATHED).getKeyFrame(state.getStateTimer(), true);
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
            case WEAPON_SHEATHING:
                textureRegion = animations.get(State.WEAPON_SHEATHING).getKeyFrame(state.getStateTimer(), false);
                break;
            case WEAPON_UNSHEATHING:
                textureRegion = animations.get(State.WEAPON_UNSHEATHING).getKeyFrame(state.getStateTimer(), false);
                break;
            case SKILL:
                textureRegion = animations.get(State.SKILL).getKeyFrame(state.getStateTimer(), true);
                break;
            case KILLED:
                textureRegion = animations.get(State.KILLED).getKeyFrame(state.getStateTimer(), false);
                break;
            case IDLE_SHEATHED:
                textureRegion = animations.get(State.IDLE_SHEATHED).getKeyFrame(state.getStateTimer(), true);
                break;
            case IDLE_UNSHEATHED: // fall through.
            default:
                textureRegion = animations.get(State.IDLE_UNSHEATHED).getKeyFrame(state.getStateTimer(), true);
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

        return textureRegion;
    }

    /**
     * Determines which state the character currently is in.
     * @param entity character entity.
     * @return character's current state.
     */
    protected State getState(Entity entity) {
        StateComponent state = Mappers.STATE.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);

        if (state.isSetToKill()) {
            return State.KILLED;
        } else if (state.isUsingSkill()) {
            return State.SKILL;
        } else if (state.isAttacking()) {
            return State.ATTACKING;
        } else if (state.isSheathing()) {
            return State.WEAPON_SHEATHING;
        } else if (state.isUnsheathing()) {
            return State.WEAPON_UNSHEATHING;
        } else if (state.isJumping()) {
            return State.JUMPING;
        } else if (b2body.getBody().getLinearVelocity().y < -.5f) {
            return State.FALLING;
        } else if (state.isCrouching()) {
            return State.CROUCHING;
        } else if (b2body.getBody().getLinearVelocity().x > .01f || b2body.getBody().getLinearVelocity().x < -.01f) {
            return (state.isSheathed()) ? State.RUNNING_SHEATHED : State.RUNNING_UNSHEATHED;
        } else {
            return (state.isSheathed()) ? State.IDLE_SHEATHED : State.IDLE_UNSHEATHED;
        }
    }

}
