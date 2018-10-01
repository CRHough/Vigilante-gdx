package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.CharacterAIComponent;
import com.aesophor.medievania.component.character.DroppableItemsComponent;
import com.aesophor.medievania.util.CategoryBits;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Character {

    public Enemy(String enemyName, AssetManager assets, World world, float x, float y) {
        super(enemyName, assets, world, x, y);

        add(new CharacterAIComponent());
        add(new DroppableItemsComponent(Mappers.CHARACTER_DATA.get(this).getItems()));

        // Create body and fixtures.
        short bodyCategoryBits = CategoryBits.ENEMY;
        short bodyMaskBits = CategoryBits.WALL | CategoryBits.PLAYER | CategoryBits.MELEE_WEAPON | CategoryBits.CLIFF_MARKER;
        short feetMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL;
        short weaponMaskBits = CategoryBits.PLAYER | CategoryBits.OBJECT;
        defineBody(BodyDef.BodyType.DynamicBody, bodyCategoryBits, bodyMaskBits, feetMaskBits, weaponMaskBits);

        Mappers.STATE.get(this).setFacingRight(false);
    }


    @Override
    public void receiveDamage(Character source, int damage) {
        super.receiveDamage(source, damage);
        Mappers.STATE.get(this).setAlerted(true);
    }

}