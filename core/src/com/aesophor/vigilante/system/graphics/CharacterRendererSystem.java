package com.aesophor.vigilante.system.graphics;

import com.aesophor.vigilante.component.Mappers;
import com.aesophor.vigilante.component.character.CharacterAnimationsComponent;
import com.aesophor.vigilante.component.character.CharacterState;
import com.aesophor.vigilante.component.character.CharacterStateComponent;
import com.aesophor.vigilante.component.character.CharacterStatsComponent;
import com.aesophor.vigilante.component.physics.B2BodyComponent;
import com.aesophor.vigilante.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * CharacterRendererSystem contains base method to generate body/equipment
 * key frames (animations) for a character.
 *
 * Bodies will be rendered first, and then equipment will be rendered on top of them.
 * See BodyRendererSystem and EquipmentRendererSystem.
 */
public abstract class CharacterRendererSystem extends IteratingSystem {

    protected final Batch batch;
    protected final Camera camera;
    protected final World world;

    public CharacterRendererSystem(Batch batch, Camera camera, World world) {
        super(Family.all(CharacterStateComponent.class, CharacterAnimationsComponent.class).get());
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
    protected TextureRegion getFrame(Entity entity, CharacterAnimationsComponent animations, float delta) {
        CharacterStateComponent state = Mappers.STATE.get(entity);
        CharacterStatsComponent stats = Mappers.STATS.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);

        TextureRegion textureRegion;
        switch (state.getCurrentState()) {
            case RUNNING_UNSHEATHED:
                textureRegion = animations.get(CharacterState.RUNNING_UNSHEATHED).getKeyFrame(state.getStateTimer(), true);
                break;
            case RUNNING_SHEATHED:
                textureRegion = animations.get(CharacterState.RUNNING_SHEATHED).getKeyFrame(state.getStateTimer(), true);
                break;
            case JUMPING_UNSHEATHED:
                textureRegion = animations.get(CharacterState.JUMPING_UNSHEATHED).getKeyFrame(state.getStateTimer(), false);
                break;
            case JUMPING_SHEATHED:
                textureRegion = animations.get(CharacterState.JUMPING_SHEATHED).getKeyFrame(state.getStateTimer(), false);
                break;
            case FALLING_UNSHEATHED:
                textureRegion = animations.get(CharacterState.FALLING_UNSHEATHED).getKeyFrame(state.getStateTimer(), false);
                break;
            case FALLING_SHEATHED:
                textureRegion = animations.get(CharacterState.FALLING_SHEATHED).getKeyFrame(state.getStateTimer(), false);
                break;
            case CROUCHING_UNSHEATHED:
                textureRegion = animations.get(CharacterState.CROUCHING_UNSHEATHED).getKeyFrame(state.getStateTimer(), false);
                break;
            case CROUCHING_SHEATHED:
                textureRegion = animations.get(CharacterState.CROUCHING_SHEATHED).getKeyFrame(state.getStateTimer(), false);
                break;
            case ATTACKING:
                textureRegion = animations.get(CharacterState.ATTACKING).getKeyFrame(state.getStateTimer(), false);
                break;
            case WEAPON_SHEATHING:
                textureRegion = animations.get(CharacterState.WEAPON_SHEATHING).getKeyFrame(state.getStateTimer(), false);
                break;
            case WEAPON_UNSHEATHING:
                textureRegion = animations.get(CharacterState.WEAPON_UNSHEATHING).getKeyFrame(state.getStateTimer(), false);
                break;
            case SKILL:
                textureRegion = animations.get(CharacterState.SKILL).getKeyFrame(state.getStateTimer(), true);
                break;
            case KILLED:
                textureRegion = animations.get(CharacterState.KILLED).getKeyFrame(state.getStateTimer(), false);
                break;
            case IDLE_SHEATHED:
                textureRegion = animations.get(CharacterState.IDLE_SHEATHED).getKeyFrame(state.getStateTimer(), true);
                break;
            case IDLE_UNSHEATHED: // fall through.
            default:
                textureRegion = animations.get(CharacterState.IDLE_UNSHEATHED).getKeyFrame(state.getStateTimer(), true);
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
    protected CharacterState getState(Entity entity) {
        CharacterStateComponent state = Mappers.STATE.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);

        if (state.isSetToKill()) {
            return CharacterState.KILLED;
        } else if (state.isUsingSkill()) {
            return CharacterState.SKILL;
        } else if (state.isAttacking()) {
            return CharacterState.ATTACKING;
        } else if (state.isSheathing()) {
            return CharacterState.WEAPON_SHEATHING;
        } else if (state.isUnsheathing()) {
            return CharacterState.WEAPON_UNSHEATHING;
        } else if (state.isJumping()) {
            return (state.isSheathed()) ? CharacterState.JUMPING_SHEATHED : CharacterState.JUMPING_UNSHEATHED;
        } else if (b2body.getBody().getLinearVelocity().y < -.5f) {
            return (state.isSheathed()) ? CharacterState.FALLING_SHEATHED : CharacterState.FALLING_UNSHEATHED;
        } else if (state.isCrouching()) {
            return (state.isSheathed()) ? CharacterState.CROUCHING_SHEATHED : CharacterState.CROUCHING_UNSHEATHED;
        } else if (b2body.getBody().getLinearVelocity().x > .01f || b2body.getBody().getLinearVelocity().x < -.01f) {
            return (state.isSheathed()) ? CharacterState.RUNNING_SHEATHED : CharacterState.RUNNING_UNSHEATHED;
        } else {
            return (state.isSheathed()) ? CharacterState.IDLE_SHEATHED : CharacterState.IDLE_UNSHEATHED;
        }
    }

}
