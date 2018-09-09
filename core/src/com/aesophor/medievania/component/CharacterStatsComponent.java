package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;

public class CharacterStatsComponent implements Component {

    public String name;
    public int level;
    public int exp;

    public int fullHealth;
    public int fullStamina;
    public int fullMagicka;

    public int health;
    public int stamina;
    public int magicka;

    public float bodyHeight;
    public float bodyWidth;

    public float movementSpeed;
    public float jumpHeight;
    public float attackForce;
    public float attackTime;
    public int attackRange;
    public int attackDamage;

}