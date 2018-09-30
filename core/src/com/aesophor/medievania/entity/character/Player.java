package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.*;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.combat.CharacterKilledEvent;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Character {

    private static final float POST_DAMAGE_INVINCIBLE_DURATION = 1.5f;

    public Player(AssetManager assets, World world, float x, float y) {
        super("Player", assets, world, x, y);

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

        Mappers.SPRITE.get(this).setBounds(0, 0, 105f / Constants.PPM, 105f / Constants.PPM);
        Mappers.STATE.get(this).setSheathed(true);
    }


    public void reposition(Vector2 position) {
        Mappers.B2BODY.get(this).getBody().setTransform(position, 0);
    }

    public void reposition(float x, float y) {
        Mappers.B2BODY.get(this).getBody().setTransform(x, y, 0);
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
        CharacterStateComponent state = Mappers.STATE.get(this);

        super.receiveDamage(source, damage);
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