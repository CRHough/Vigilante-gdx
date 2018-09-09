package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.EnemyAIComponent;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Character {

    public Enemy(Texture texture, World world, float x, float y) {
        super(texture, world, x, y);
        add(new EnemyAIComponent());
    }


    @Override
    public void receiveDamage(Character source, int damage) {
        super.receiveDamage(source, damage);
        state.alerted = true;
    }

}