package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {

    private State previousState;
    private State currentState;
    private float time;

    private boolean alerted;
    private boolean facingRight;
    private boolean jumping;
    private boolean onPlatform;
    private boolean attacking;
    private boolean crouching;
    private boolean invincible;
    private boolean killed;
    private boolean setToKill;

    public StateComponent(State defaultState) {
        this.currentState = defaultState;
        facingRight = true;
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

    public float getTime() {
        return time;
    }

    public void update(float delta) {
        time += delta;
    }

    public void resetTime() {
        time = 0;
    }


    public boolean isAlerted() {
        return alerted;
    }

    public boolean facingRight() {
        return facingRight;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public boolean isSetToKill() {
        return setToKill;
    }

    public boolean isKilled() {
        return killed;
    }

    public boolean isJumping() {
        return jumping;
    }

    public boolean isOnPlatform() {
        return onPlatform;
    }


    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setOnPlatform(boolean onPlatform) {
        this.onPlatform = onPlatform;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public void setSetToKill(boolean setToKill) {
        this.setToKill = setToKill;
    }
}