package com.aesophor.medievania.system;

import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.ControllableComponent;
import com.aesophor.medievania.component.StateComponent;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.map.PortalUsedEvent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class PlayerControlSystem extends IteratingSystem {

    public PlayerControlSystem() {
        super(Family.all(ControllableComponent.class).get());
    }


    private void handleInput(Player player, StateComponent state) {
        if (state.isSetToKill()) {
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            player.swingWeapon();
        }

        if (state.isCrouching() && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.getUp();
        }

        // When player is attacking, movement is disabled.
        if (!state.isAttacking()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
                if (state.isCrouching()) {
                    player.jumpDown();
                } else {
                    player.jump();
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
                player.forwardRush();
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.moveLeft();
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.crouch();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                if (player.getCurrentPortal() != null && !state.isSetToKill()) {
                    GameEventManager.getInstance().fireEvent(new PortalUsedEvent(player.getCurrentPortal()));
                }
            }
        }
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        handleInput((Player) entity, Mappers.STATE.get(entity));
    }

}