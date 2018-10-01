package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.*;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.combat.CharacterKilledEvent;
import com.aesophor.medievania.util.CategoryBits;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Character {

    private static final String JSON_PLAYER_IDENTIFIER = "Player"; // Used to identify player data from characters.json.
    private static final float POST_DAMAGE_INVINCIBLE_DURATION = 1.5f;

    public Player(AssetManager assets, World world, float x, float y) {
        super(JSON_PLAYER_IDENTIFIER, assets, world, x, y);

        add(new PickupItemTargetComponent());
        add(new ControllableComponent());
        add(new PortalTargetComponent());
        add(new EquipmentSlotsComponent());

        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.PLAYER;
        short bodyMaskBits = CategoryBits.PORTAL | CategoryBits.ENEMY | CategoryBits.MELEE_WEAPON | CategoryBits.ITEM;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL;
        short weaponMaskBits = CategoryBits.ENEMY | CategoryBits.OBJECT;
        defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);

        Mappers.STATE.get(this).setSheathed(true);
    }


    @Override
    public void inflictDamage(Character target, int damage) {
        super.inflictDamage(target, damage);

        if (Mappers.STATE.get(target).isSetToKill()) {
            GameEventManager.getInstance().fireEvent(new CharacterKilledEvent(this, target));
        }
    }

    @Override
    public void receiveDamage(Character source, int damage) {
        super.receiveDamage(source, damage);

        CharacterStateComponent state = Mappers.STATE.get(this);
        state.setInvincible(true);

        Timer.schedule(new Task() {
            @Override
            public void run() {
                if (!state.isSetToKill()) {
                    state.setInvincible(false);
                }
            }
        }, POST_DAMAGE_INVINCIBLE_DURATION);
    }

}