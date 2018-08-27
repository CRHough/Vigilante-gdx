package com.aesophor.medievania.constants;

public class CategoryBits {

    public static final short GROUND = 1;
    public static final short CLIFF_MARKER = 2;
    public static final short OBJECT = 4;
    public static final short INVINCIBLE = 8;
    public static final short DESTROYED = 16;
    public static final short PLAYER = 32;
    public static final short ENEMY = 64;
    public static final short MELEE_WEAPON = 128;
    
    private CategoryBits() {
        throw new AssertionError();
    }
    
}