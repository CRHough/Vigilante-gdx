package com.aesophor.medievania.event.combat;

import com.aesophor.medievania.entity.character.Enemy;
import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventType;

public class EnemyDiedEvent extends GameEvent {

    private Enemy enemy;

    public EnemyDiedEvent(Enemy enemy) {
        super(GameEventType.ENEMY_DIED);
        this.enemy = enemy;
    }


    public Enemy getEnemy() {
        return enemy;
    }

}