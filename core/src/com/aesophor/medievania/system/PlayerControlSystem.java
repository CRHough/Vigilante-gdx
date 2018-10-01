package com.aesophor.medievania.system;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.ControllableComponent;
import com.aesophor.medievania.component.character.PickupItemTargetComponent;
import com.aesophor.medievania.component.character.PortalTargetComponent;
import com.aesophor.medievania.component.character.CharacterStateComponent;
import com.aesophor.medievania.component.equipment.EquipmentType;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.map.PortalUsedEvent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class PlayerControlSystem extends IteratingSystem {

    private final PooledEngine engine;

    public PlayerControlSystem(PooledEngine engine) {
        super(Family.all(ControllableComponent.class).get());
        this.engine = engine;
    }


    private void handleInput(Player player, CharacterStateComponent state) {
        if (state.isSetToKill()) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            if (!state.isSheathed()) {
                player.attack();
            } else {
                player.unsheathWeapon();
            }
        }

        if (state.isCrouching() && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.getUp();
        }

        // When player is attacking, movement is disabled.
        if (!state.isAttacking()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                if (state.isSheathed()) {
                    if (Mappers.EQUIPMENT_SLOTS.get(player).has(EquipmentType.WEAPON)) {
                        player.unsheathWeapon();
                    }
                } else {
                    player.sheathWeapon();
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
                if (state.isCrouching()) {
                    player.jumpDown();
                } else {
                    player.jump();
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                PickupItemTargetComponent pickupItemTarget = Mappers.PICKUP_ITEM_TARGETS.get(player);
                if (pickupItemTarget.hasInRangeItem()) {
                    player.pickup(pickupItemTarget.getInRangeItems().first());
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                player.batPower();
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (!Mappers.STATE.get(player).isSheathing() && !Mappers.STATE.get(player).isUnsheathing()) {
                    player.moveRight();
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (!Mappers.STATE.get(player).isSheathing() && !Mappers.STATE.get(player).isUnsheathing()) {
                    player.moveLeft();
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.crouch();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                PortalTargetComponent portalTarget = Mappers.PORTAL_TARGET.get(player);
                if (portalTarget.getInRangePortal() != null && !state.isSetToKill()) {
                    GameEventManager.getInstance().fireEvent(new PortalUsedEvent(portalTarget.getInRangePortal()));
                }
            }
        }
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        handleInput((Player) entity, Mappers.STATE.get(entity));
    }

}