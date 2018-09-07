package com.aesophor.medievania.system;

import com.aesophor.medievania.character.Player;
import com.aesophor.medievania.component.CharacterStatsComponent;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.PlayerComponent;
import com.aesophor.medievania.component.StateComponent;
import com.aesophor.medievania.event.PortalUsedEvent;
import com.aesophor.medievania.map.Portal;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Timer;

public class PlayerControlSystem extends IteratingSystem {

    private StateComponent state;

    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class).get());
    }


    private void handleInput(Player player) {
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
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {

            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.moveLeft();
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.crouch();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                if (player.getCurrentPortal() != null && !state.isSetToKill()) {
                    /*
                    gameEventManager.fireEvent(new PortalUsedEvent());

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            Portal currentPortal = player.getCurrentPortal();
                            int targetPortalID = currentPortal.getTargetPortalID();

                            // Set the new map and reposition the player at the position of the target portal's body.
                            setGameMap(currentPortal.getTargetMap());
                            player.reposition(currentMap.getPortals().get(targetPortalID).getBody().getPosition());
                        }
                    }, ScreenFadeSystem.FADEIN_DURATION);
                    */
                }
            }
        }
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        state = Mappers.STATE.get(entity);
        handleInput((Player) entity);
    }

}