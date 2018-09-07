package com.aesophor.medievania.system;

import com.aesophor.medievania.component.*;
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

    private CharacterStatsComponent stats;  // health, magicka, stamina, exp... etc.
    private B2BodyComponent b2body;         // box2d bodybuilder, body and fixtures.
    private SpriteComponent sprite;         // character's sprite.
    private AnimationComponent animations;  // character's animations.
    private StateComponent state;           // character's state.

    public CharacterRendererSystem(Batch batch, Camera camera, World world) {
        super(Family.all(SpriteComponent.class).get()); // look into this later?

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
        stats = Mappers.CHARACTER_STATS.get(entity);
        b2body = Mappers.B2BODY.get(entity);
        sprite = Mappers.SPRITE.get(entity);
        animations = Mappers.ANIMATION.get(entity);
        state = Mappers.STATE.get(entity);

        if (!state.killed) {
            // If the character's health has reached zero but hasn't die yet,
            // it means that the killedAnimation is not fully played.
            // So here we'll play it until it's finished.
            if (state.setToKill) {
                sprite.sprite.setRegion(getFrame(delta));

                // Set killed to true to prevent further rendering updates.
                if (animations.get(State.KILLED).isAnimationFinished(state.time)) {
                    world.destroyBody(b2body.body);
                    state.killed = true;
                }
            } else {
                sprite.sprite.setRegion(getFrame(delta));

                // Set attacking back to false, implying the attack has completed.
                if (state.attacking && state.time >= stats.attackTime) {
                    state.attacking = false;
                    state.time = 0;
                }
            }

            float textureX = b2body.body.getPosition().x - sprite.sprite.getWidth() / 2;
            float textureY = b2body.body.getPosition().y - sprite.sprite.getHeight() / 2 + (10 / Constants.PPM);
            sprite.sprite.setPosition(textureX, textureY);
        }

        sprite.sprite.setRegion(getFrame(delta));
        sprite.sprite.draw(batch);
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
                textureRegion = animations.get(State.RUNNING).getKeyFrame(state.time, true);
                break;
            case JUMPING:
                textureRegion = animations.get(State.JUMPING).getKeyFrame(state.time, false);
                break;
            case FALLING:
                textureRegion = animations.get(State.FALLING).getKeyFrame(state.time, false);
                break;
            case CROUCHING:
                textureRegion = animations.get(State.CROUCHING).getKeyFrame(state.time, false);
                break;
            case ATTACKING:
                textureRegion = animations.get(State.ATTACKING).getKeyFrame(state.time, false);
                break;
            case KILLED:
                textureRegion = animations.get(State.KILLED).getKeyFrame(state.time, false);
                break;
            case IDLE:
            default:
                textureRegion = animations.get(State.IDLE).getKeyFrame(state.time, true);
                break;
        }

        if (!state.facingRight && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            CircleShape shape = (CircleShape) b2body.meleeWeaponFixture.getShape();
            shape.setPosition(new Vector2(-stats.attackRange / Constants.PPM, 0));
        } else if (state.facingRight && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            CircleShape shape = (CircleShape) b2body.meleeWeaponFixture.getShape();
            shape.setPosition(new Vector2(stats.attackRange / Constants.PPM, 0));
        }

        state.time = (state.getCurrentState() != state.getPreviousState()) ? 0 : state.time + delta;
        return textureRegion;
    }

    /**
     * Determines which state the character currently is in.
     * @return character's current state.
     */
    private State getState() {
        if (state.setToKill) {
            return State.KILLED;
        } else if (state.attacking) {
            return State.ATTACKING;
        } else if (state.jumping) {
            return State.JUMPING;
        } else if (b2body.body.getLinearVelocity().y < -.5f) {
            return State.FALLING;
        } else if (state.crouching) {
            return State.CROUCHING;
        } else if (b2body.body.getLinearVelocity().x > .01f || b2body.body.getLinearVelocity().x < -.01f) {
            return State.RUNNING;
        } else {
            return State.IDLE;
        }
    }

}