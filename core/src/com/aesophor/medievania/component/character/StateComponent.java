package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {

    private State previousState;
    private State currentState;
    private float stateTimer;

    private boolean isFacingRight;
    private boolean isSheathed;
    private boolean isSheathing;
    private boolean isUnsheathing;
    private boolean isJumping;
    private boolean isOnPlatform;
    private boolean isAttacking;
    private boolean isCrouching;
    private boolean isUsingSkill;
    private boolean isInvincible;
    private boolean isAlerted;
    private boolean isKilled;
    private boolean isSetToKill;

    public StateComponent(State defaultState) {
        this.currentState = defaultState;
        isFacingRight = true;
    }


    public State getPreviousState() {
        return previousState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State newState) {
        this.previousState = this.currentState;
        this.currentState = newState;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void update(float delta) {
        stateTimer += delta;
    }

    public void resetStateTimer() {
        stateTimer = 0;
    }


    public boolean isFacingRight() {
        return isFacingRight;
    }

    public boolean isSheathed() {
        return isSheathed;
    }

    public boolean isSheathing() {
        return isSheathing;
    }

    public boolean isUnsheathing() {
        return isUnsheathing;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isOnPlatform() {
        return isOnPlatform;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isCrouching() {
        return isCrouching;
    }

    public boolean isUsingSkill() {
        return isUsingSkill;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public boolean isAlerted() {
        return isAlerted;
    }

    public boolean isKilled() {
        return isKilled;
    }

    public boolean isSetToKill() {
        return isSetToKill;
    }


    public void setFacingRight(boolean isFacingRight) {
        this.isFacingRight = isFacingRight;
    }

    public void setSheathed(boolean isSheathed) {
        this.isSheathed = isSheathed;
    }

    public void setSheathing(boolean isSheathing) {
        this.isSheathing = isSheathing;
    }

    public void setUnsheathing(boolean isUnsheathing) {
        this.isUnsheathing = isUnsheathing;
    }

    public void setJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public void setOnPlatform(boolean isOnPlatform) {
        this.isOnPlatform = isOnPlatform;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void setCrouching(boolean isCrouching) {
        this.isCrouching = isCrouching;
    }

    public void setUsingSkill(boolean isUsingSkill) {
        this.isUsingSkill = isUsingSkill;
    }

    public void setInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
    }

    public void setAlerted(boolean isAlerted) {
        this.isAlerted = isAlerted;
    }

    public void setKilled(boolean isKilled) {
        this.isKilled = isKilled;
    }

    public void setSetToKill(boolean isSetToKill) {
        this.isSetToKill = isSetToKill;
    }

}