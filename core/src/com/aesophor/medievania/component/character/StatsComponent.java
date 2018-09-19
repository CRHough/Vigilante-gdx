package com.aesophor.medievania.component.character;

import com.badlogic.ashley.core.Component;

public class StatsComponent implements Component {

    private String name;
    private int level;
    private int exp;

    private int fullHealth;
    private int fullStamina;
    private int fullMagicka;

    private int health;
    private int stamina;
    private int magicka;

    private int _str;
    private int _dex;
    private int _int;
    private int _luk;

    private float bodyHeight;
    private float bodyWidth;

    private float movementSpeed;
    private float jumpHeight;
    private float attackForce;
    private float attackTime;
    private int attackRange;
    private int attackDamage;

    public StatsComponent() {
        level = 1;
    }

    public StatsComponent(StatsComponent s) {
        name = s.getName();
        level = s.getLevel();
        exp = s.getExp();

        fullHealth = s.getFullHealth();
        fullStamina = s.getFullStamina();
        fullMagicka = s.getFullMagicka();

        modHealth(fullHealth);
        modStamina(fullStamina);
        modMagicka(fullMagicka);

        bodyHeight = s.getBodyHeight();
        bodyWidth = s.getBodyWidth();

        movementSpeed = s.getMovementSpeed();
        jumpHeight = s.getJumpHeight();
        attackForce = s.getAttackForce();
        attackTime = s.getAttackTime();
        attackRange = s.getAttackRange();
        attackDamage = s.getAttackDamage();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
    }


    public int getExp() {
        return exp;
    }

    public void modExp(int exp) {
        this.exp += exp;
    }


    public int getFullHealth() {
        return fullHealth;
    }

    public int getFullStamina() {
        return fullStamina;
    }

    public int getFullMagicka() {
        return fullMagicka;
    }


    public int getHealth() {
        return health;
    }

    public int getStamina() {
        return stamina;
    }

    public int getMagicka() {
        return magicka;
    }


    public boolean isHealthFull() {
        return health == fullHealth;
    }

    public boolean isStaminaFull() {
        return stamina == fullStamina;
    }

    public boolean isMagickaFull() {
        return magicka == fullMagicka;
    }


    public int getNextHealthRegen(int nextHealthRegen) {
        return (health + nextHealthRegen > fullHealth) ? fullHealth - health : nextHealthRegen;
    }

    public int getNextStaminaRegen(int nextStaminaRegen) {
        return (stamina + nextStaminaRegen > fullStamina) ? fullStamina - stamina : nextStaminaRegen;
    }

    public int getNextMagickaRegen(int nextMagickaRegen) {
        return (magicka + nextMagickaRegen > fullMagicka) ? fullMagicka - magicka : nextMagickaRegen;
    }


    public void modFullHealth(int health) {
        fullHealth += health;
    }

    public void modFullStamina(int stamina) {
        fullStamina += stamina;
    }

    public void modFullMagicka(int magicka) {
        fullMagicka += magicka;
    }


    public void modHealth(int health) {
        this.health += health;
        if (this.health + health < 0) {
            this.health = 0;
        }
    }

    public void modStamina(int stamina) {
        this.stamina += stamina;
        if (this.stamina + stamina < 0) {
            this.stamina = 0;
        }
    }

    public void modMagicka(int magicka) {
        this.magicka += magicka;
        if (this.magicka + magicka < 0) {
            this.magicka = 0;
        }
    }


    public float getBodyHeight() {
        return bodyHeight;
    }

    public void setBodyHeight(float bodyHeight) {
        this.bodyHeight = bodyHeight;
    }

    public float getBodyWidth() {
        return bodyWidth;
    }

    public void setBodyWidth(float bodyWidth) {
        this.bodyWidth = bodyWidth;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public float getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(float jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public float getAttackForce() {
        return attackForce;
    }

    public void setAttackForce(float attackForce) {
        this.attackForce = attackForce;
    }

    public float getAttackTime() {
        return attackTime;
    }

    public void setAttackTime(float attackTime) {
        this.attackTime = attackTime;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

}