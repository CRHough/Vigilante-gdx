package com.aesophor.medievania.component.character;

/**
 * All possible character states.
 *
 * The system responsible for deciding which state a character currently is in
 * is com.aesophor.medievania.system.graphics.BodyRendererSystem.
 */
public enum CharacterState {

    IDLE_SHEATHED,
    IDLE_UNSHEATHED,

    WEAPON_SHEATHING,
    WEAPON_UNSHEATHING,

    RUNNING_SHEATHED,
    RUNNING_UNSHEATHED,

    JUMPING_SHEATHED,
    JUMPING_UNSHEATHED,

    FALLING_SHEATHED,
    FALLING_UNSHEATHED,

    CROUCHING_SHEATHED,
    CROUCHING_UNSHEATHED,

    ATTACKING,
    SKILL,
    KILLED;

}