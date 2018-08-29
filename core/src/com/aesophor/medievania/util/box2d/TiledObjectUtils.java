package com.aesophor.medievania.util.box2d;

import box2dLight.RayHandler;
import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.map.GameMapLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class TiledObjectUtils {

    private static final boolean GROUND_COLLIDABLE = true;
    private static final boolean PLATFORM_COLLIDABLE = true;
    private static final boolean WALL_COLLIDABLE = true;
    private static final boolean PORTAL_COLLIDABLE = false;
    private static final boolean CLIFF_MARKER_COLLIDABLE = false;

    private static final Color LIGHT_COLOR = Color.ORANGE;
    private static final float LIGHT_DISTANCE = 80;

    /**
     * Parses the layers of the specified TiledMap, and creates the corresponding bodies.
     * @param tiledMap the tiled map to parse.
     * @param world the world where bodies will be created.
     */
    public static void parseLayers(World world, TiledMap tiledMap, RayHandler rayHandler) {
        createPolylines(world, tiledMap, GameMapLayer.GROUND, CategoryBits.GROUND, GROUND_COLLIDABLE, Constants.GROUND_FRICTION);
        createRectangles(world, tiledMap, GameMapLayer.PLATFORM, CategoryBits.PLATFORM, PLATFORM_COLLIDABLE, Constants.GROUND_FRICTION);
        createPolylines(world, tiledMap, GameMapLayer.WALL, CategoryBits.WALL, WALL_COLLIDABLE, 0);
        createRectangles(world, tiledMap, GameMapLayer.PORTAL, CategoryBits.PORTAL, PORTAL_COLLIDABLE, 0);
        createPolylines(world, tiledMap, GameMapLayer.CLIFF_MARKER, CategoryBits.CLIFF_MARKER, CLIFF_MARKER_COLLIDABLE, 0);
        createLightSources(rayHandler, tiledMap, GameMapLayer.LIGHT_SOURCE);
    }

    private static void createRectangles(World world, TiledMap tiledMap, GameMapLayer layer,
                                         short categoryBits, boolean collidable, float friction) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);

        MapObjects mapObjects = tiledMap.getLayers().get(layer.ordinal()).getObjects();
        for (MapObject o : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();
            Vector2 centerPos = new Vector2(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(centerPos, Constants.PPM)
                    .buildBody();

            bodyBuilder.newRectangleFixture(centerPos, rect.getWidth() / 2, rect.getHeight() / 2, Constants.PPM)
                    .categoryBits(categoryBits)
                    .friction(friction)
                    .isSensor(!collidable)
                    .buildFixture();
        }
    }

    private static void createPolylines(World world, TiledMap tiledMap, GameMapLayer layer,
                                        short categoryBits, boolean collidable, float friction) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);

        MapObjects mapObjects = tiledMap.getLayers().get(layer.ordinal()).getObjects();
        for (MapObject o : mapObjects.getByType(PolylineMapObject.class)) {
            float[] vertices = ((PolylineMapObject) o).getPolyline().getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length / 2];

            for (int i = 0; i < worldVertices.length; i++) {
                worldVertices[i] = new Vector2(vertices[i * 2], vertices[i * 2 + 1]);
            }


            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(0, 0, Constants.PPM)
                    .buildBody();

            bodyBuilder.newPolylineFixture(worldVertices, Constants.PPM)
                    .categoryBits(categoryBits)
                    .friction(friction)
                    .isSensor(!collidable)
                    .buildFixture();
        }
    }

    private static void createLightSources(RayHandler rayHandler, TiledMap tiledMap, GameMapLayer layer) {
        MapObjects mapObjects = tiledMap.getLayers().get(layer.ordinal()).getObjects();
        for (MapObject o : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();
            float x = rect.getX() + rect.getWidth() / 2;
            float y = rect.getY() + rect.getHeight() / 2;

            LightBuilder.createPointLight(rayHandler, LIGHT_COLOR, LIGHT_DISTANCE, x, y, Constants.PPM);
        }
    }

}
