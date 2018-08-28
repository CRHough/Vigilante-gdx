package com.aesophor.medievania.world.character;

public interface EnemyAttackListener<E extends Character> {

    public void setOnAttack(E enemy);
    
}