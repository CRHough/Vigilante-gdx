package com.aesophor.medievania.constant;

public final class Constants {

    // Graphics constants
    public static final float PPM = 100;
    public static final int V_WIDTH = 500;
    public static final int V_HEIGHT = 250;
    
    // Physics constants
    public static final int GRAVITY = -10;
    
    // CollisionBit constants
    public static final short GROUND_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short MELEE_WEAPON_BIT = 128;
    
    // TiledMap layer constants
    public static final int GROUND_LAYER = 0;
    
    
    private Constants() {
        throw new AssertionError();
    }
    
}