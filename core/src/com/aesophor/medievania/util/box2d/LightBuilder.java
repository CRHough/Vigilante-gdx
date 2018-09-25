package com.aesophor.medievania.util.box2d;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.aesophor.medievania.util.CategoryBits;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

public class LightBuilder {

    private static final short LIGHT_MASKBITS = CategoryBits.GROUND | CategoryBits.PLATFORM | CategoryBits.WALL
            | CategoryBits.PLAYER | CategoryBits.ENEMY | CategoryBits.ITEM | CategoryBits.OBJECT;

    public static PointLight createPointLight(RayHandler rayHandler, Color color, float dist, Body body, float ppm) {
        PointLight pl = new PointLight(rayHandler, 120, color, dist / ppm, 0, 0);
        pl.setSoftnessLength(0f);
        pl.attachToBody(body);
        pl.setXray(false);
        return pl;
    }

    public static PointLight createPointLight(RayHandler rayHandler, Color color, float dist, float x, float y, float ppm) {
        PointLight pl = new PointLight(rayHandler, 120, color, dist / ppm, x / ppm, y / ppm);
        pl.setContactFilter((short) -1, (short) 0, LIGHT_MASKBITS);
        pl.setSoftnessLength(0f);
        pl.setXray(false);
        return pl;
    }

}
