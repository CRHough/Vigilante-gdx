package com.aesophor.medievania.world.character;

import java.util.concurrent.ThreadLocalRandom;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.math.Vector2;

public class Behavior {

    private Character character;

    private int moveDirection;
    private float moveDuration;
    private float moveTimer;
    private float sleepDuration;
    private float sleepTimer;

    private Vector2 lastStoppedPosition;
    private float lastTraveledDistance;
    private float calculateDistanceTimer;
    private float checkStuckedInterval;

    public Behavior(Character character) {
        this.character = character;
        lastStoppedPosition = new Vector2();
        checkStuckedInterval = .1f;
    }


    /**
     * Makes the character move randomly, either to its left or to its right.
     * The character will move for a given period of time, and then sleep for another given period of time.
     * @param delta delta time.
     * @param minMoveDuration minimum amount of time the character will keep moving.
     * @param maxMoveDuration maximum amount of time the character will keep moving.
     * @param minSleepDuration minimum amount of time the character will sleep after moving.
     * @param maxSleepDuration maximum amount of time the character will sleep after moving.
     */
    public void moveRandomly(float delta, int minMoveDuration, int maxMoveDuration, int minSleepDuration, int maxSleepDuration) {
        if (sleepTimer >= sleepDuration) {
            moveDirection = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            moveDuration = ThreadLocalRandom.current().nextInt(minMoveDuration, maxMoveDuration + 1);
            sleepDuration = ThreadLocalRandom.current().nextInt(minSleepDuration, maxSleepDuration + 1);

            moveTimer = 0;
            sleepTimer = 0;
        }

        switch (moveDirection) {
            case 0:
                if (moveTimer < moveDuration) {
                    character.moveLeft();
                    jumpIfStucked(delta);
                    moveTimer += delta;
                } else {
                    if (sleepTimer < sleepDuration) {
                        sleepTimer += delta;
                    }
                }
                break;

            case 1:
                if (moveTimer < moveDuration) {
                    character.moveRight();
                    jumpIfStucked(delta);
                    moveTimer += delta;
                } else {
                    if (sleepTimer < sleepDuration) {
                        sleepTimer += delta;
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * Reverses the current moving direction of character. Must be used with moveRandomly().
     */
    public void reverseMovement() {
        moveDirection = (moveDirection == 0) ? 1 : 0;
    }


    public void jumpIfStucked(float delta) {
        if (calculateDistanceTimer > checkStuckedInterval) {
            lastTraveledDistance = Utils.getDistance(character.getB2Body().getPosition().x, lastStoppedPosition.x);
            lastStoppedPosition.set(character.getB2Body().getPosition());

            if (lastTraveledDistance == 0) {
                character.jump();
            }

            calculateDistanceTimer = 0;
        } else {
            calculateDistanceTimer += delta;
        }
    }


    /**
     * Moves toward the specified target.
     * @param c the target character to move to.
     */
    public void moveTowardTarget(Character c) {
        if (character.getB2Body().getPosition().x > c.getB2Body().getPosition().x) {
            character.moveLeft();
        } else {
            character.moveRight();
        }
    }

}