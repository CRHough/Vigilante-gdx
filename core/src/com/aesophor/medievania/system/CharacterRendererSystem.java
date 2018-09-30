package com.aesophor.medievania.system;

import com.aesophor.medievania.component.*;
import com.aesophor.medievania.component.character.*;
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

/**
 * CharacterRendererSystem is responsible for rendering characters based on their states,
 * and it will determine which state should a character should be in at any moment.
 * TODO: Define state precedence.
 */
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
                //sprite.setRegion(getFrame(delta));

                // Set killed to true to prevent further rendering updates.
                if (animations.get(State.KILLED).isAnimationFinished(state.getStateTimer())) {
                    world.destroyBody(b2body.getBody());
                    state.setKilled(true);
                }
            } else {
                //sprite.setRegion(getFrame(delta));

                // Set attacking back to false, implying the attack has completed.
                if (state.isAttacking() && state.getStateTimer() >= stats.getAttackTime()) {
                    state.setAttacking(false);
                    state.resetStateTimer();
                }

                if (state.isSheathing() && state.getStateTimer() >= .9f) {
                    System.out.println("sheath finished: " + state.getStateTimer());
                    state.setSheathing(false);
                    state.setSheathed(true);
                }

                if (state.isUnsheathing() && state.getStateTimer() >= .9f) {
                    System.out.println("unsheath finished: " + state.getStateTimer());
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

        sprite.setRegion(getFrame(delta));
        sprite.draw(batch);


        // Clean up this pile of dog shit......... =~=
        EquipmentSlotsComponent slots = Mappers.EQUIPMENT_SLOTS.get(entity);
        slots.getEquipment().forEach(((equipmentType, item) -> {
            if (item != null) {
                CharacterAnimationComponent equipAni = Mappers.CHARACTER_ANIMATIONS.get(item);
                TextureRegion textureRegion;
                switch (state.getCurrentState()) {
                    case RUNNING_UNSHEATHED:
                        textureRegion = equipAni.get(State.RUNNING_UNSHEATHED).getKeyFrame(state.getStateTimer(), true);
                        break;
                    case RUNNING_SHEATHED:
                        textureRegion = equipAni.get(State.RUNNING_SHEATHED).getKeyFrame(state.getStateTimer(), true);
                        break;
                    case JUMPING:
                        textureRegion = equipAni.get(State.JUMPING).getKeyFrame(state.getStateTimer(), false);
                        break;
                    case FALLING:
                        textureRegion = equipAni.get(State.FALLING).getKeyFrame(state.getStateTimer(), false);
                        break;
                    case CROUCHING:
                        textureRegion = equipAni.get(State.CROUCHING).getKeyFrame(state.getStateTimer(), false);
                        break;
                    case ATTACKING:
                        textureRegion = equipAni.get(State.ATTACKING).getKeyFrame(state.getStateTimer(), false);
                        break;
                    case WEAPON_SHEATHING:
                        textureRegion = equipAni.get(State.WEAPON_SHEATHING).getKeyFrame(state.getStateTimer(), false);
                        break;
                    case WEAPON_UNSHEATHING:
                        textureRegion = equipAni.get(State.WEAPON_UNSHEATHING).getKeyFrame(state.getStateTimer(), false);
                        break;
                    case SKILL:
                        textureRegion = equipAni.get(State.SKILL).getKeyFrame(state.getStateTimer(), true);
                        break;
                    case KILLED:
                        textureRegion = equipAni.get(State.KILLED).getKeyFrame(state.getStateTimer(), false);
                        break;
                    case IDLE_SHEATHED:
                        textureRegion = equipAni.get(State.IDLE_SHEATHED).getKeyFrame(state.getStateTimer(), true);
                        break;
                    case IDLE_UNSHEATHED: // fall through.
                    default:
                        textureRegion = equipAni.get(State.IDLE_UNSHEATHED).getKeyFrame(state.getStateTimer(), true);
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

                float textureOffsetX = characterData.getTextureOffsetX();
                float textureOffsetY = characterData.getTextureOffsetY();

                float textureX = b2body.getBody().getPosition().x - sprite.getWidth() / 2 + (textureOffsetX / Constants.PPM);
                float textureY = b2body.getBody().getPosition().y - sprite.getHeight() / 2 + (textureOffsetY / Constants.PPM);

                Mappers.SPRITE.get(item).setRegion(textureRegion);
                Mappers.SPRITE.get(item).setBounds(0, 0, 105f / Constants.PPM, 105f / Constants.PPM);
                Mappers.SPRITE.get(item).setPosition(textureX, textureY);

                Mappers.SPRITE.get(item).draw(batch);
            }
        }));
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

        if (state.getCurrentState() != state.getPreviousState()) {
            state.resetStateTimer();
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