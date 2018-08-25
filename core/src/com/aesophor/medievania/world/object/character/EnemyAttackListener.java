package com.aesophor.medievania.world.object.character;

public interface EnemyAttackListener<E extends Character> {

    public void setOnAttack(E enemy);
    
}