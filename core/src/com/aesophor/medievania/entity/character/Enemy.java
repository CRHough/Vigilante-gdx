package com.aesophor.medievania.entity.character;

import com.aesophor.medievania.component.CharacterAIComponent;
import com.aesophor.medievania.component.EnemyComponent;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.combat.EnemyDiedEvent;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Character {

    public Enemy(Texture texture, World world, float x, float y) {
        super(texture, world, x, y);
        add(new EnemyComponent());
        add(new CharacterAIComponent());
    }


    @Override
    public void receiveDamage(Character source, int damage) {
        super.receiveDamage(source, damage);
        state.setAlerted(true);

        if (stats.getHealth() == 0) {
            GameEventManager.getInstance().fireEvent(new EnemyDiedEvent(this));
        }
    }

}