package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {

    public enum State { IDLE, RUNNING, JUMPING, FALLING, CROUCHING, ATTACKING, KILLED };

    private State state;
    public float time;
    public boolean isLooping;

    public StateComponent(State defaultState) {
        this.state = defaultState;
    }


    public void set(State newState) {
        this.state = newState;
    }

    public State get() {
        return state;
    }

}