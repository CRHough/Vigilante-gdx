package com.aesophor.medievania.util.box2d;

import box2dLight.RayHandler;
import com.aesophor.medievania.GameMapManager;
import com.aesophor.medievania.map.GameMap;
import com.aesophor.medievania.map.GameMapLayer;
import com.aesophor.medievania.map.Portal;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class TiledObjectUtils {

    private static final boolean GROUND_COLLIDABLE = true;
    private static final boolean PLATFORM_COLLIDABLE = true;
    private static final boolean WALL_COLLIDABLE = true;
    private static final boolean CLIFF_MARKER_COLLIDABLE = false;

    private static final Color LIGHT_COLOR = Color.ORANGE;
    private static final float LIGHT_DISTANCE = 80;

    /**
     * Parses the layers of the specified TiledMap, and creates the corresponding bodies.
     * @param world the world where all layer bodies will be built.
     * @param rayHandler the RayHandler to handle lighting.
     * @param gameMap the tiled map to parse.
     */
    public static void parseLayers(World world, RayHandler rayHandler, GameMap gameMap) {
        createPolylines(world, gameMap, GameMapLayer.GROUND, CategoryBits.GROUND, GROUND_COLLIDABLE, Constants.GROUND_FRICTION);
        createRectangles(world, gameMap, GameMapLayer.PLATFORM, CategoryBits.PLATFORM, PLATFORM_COLLIDABLE, Constants.GROUND_FRICTION);
        createPolylines(world, gameMap, GameMapLayer.WALL, CategoryBits.WALL, WALL_COLLIDABLE, 0);
        createPolylines(world, gameMap, GameMapLayer.CLIFF_MARKER, CategoryBits.CLIFF_MARKER, CLIFF_MARKER_COLLIDABLE, 0);
        createPortals(world, gameMap, GameMapLayer.PORTAL);
        createLightSources(rayHandler, gameMap, GameMapLayer.LIGHT_SOURCE);
    }

    private static void createRectangles(World world, GameMap gameMap, GameMapLayer layer,
                                         short categoryBits, boolean collidable, float friction) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);
        MapObjects mapObjects = gameMap.getTiledMap().getLayers().get(layer.ordinal()).getObjects();

        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
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

    private static void createPolylines(World world, GameMap gameMap, GameMapLayer layer,
                                        short categoryBits, boolean collidable, float friction) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);
        MapObjects mapObjects = gameMap.getTiledMap().getLayers().get(layer.ordinal()).getObjects();

        for (PolylineMapObject object : mapObjects.getByType(PolylineMapObject.class)) {
            float[] vertices = object.getPolyline().getTransformedVertices();
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

    private static void createPortals(World world, GameMap gameMap, GameMapLayer layer) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);
        MapObjects mapObjects = gameMap.getTiledMap().getLayers().get(layer.ordinal()).getObjects();
        Array<RectangleMapObject> objects = mapObjects.getByType(RectangleMapObject.class);

        for (int i = 0; i < objects.size; i++) {
            Rectangle rect = objects.get(i).getRectangle();
            Vector2 centerPos = new Vector2(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            // There are custom properties inside .tmx maps.
            // targetMap specifies which
            String targetMap = (String) objects.get(i).getProperties().get("targetMap");
            int targetPortalID = (Integer) objects.get(i).getProperties().get("targetPortalID");

            Body body = bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(centerPos, Constants.PPM)
                    .buildBody();

            // Here we add the portal to an Array inside this GameMap, so later on if we want to
            // position a character at a specific portal, we can use something like
            // "Portal p = player.getCurrentPortal();"
            // "int targetPortalID = p.getTargetPortalID();"
            // "character.reposition( currentMap.getPortals().get(targetPortalID).getBody().getPosition() );".
            Portal portal = new Portal(targetMap, targetPortalID, body);
            gameMap.getPortals().add(portal);

            // The same portal object is set as UserData, so when a character collides with
            // a portal body, then we can get that portal by using "character.getCurrentPortal()"
            bodyBuilder.newRectangleFixture(centerPos, rect.getWidth() / 2, rect.getHeight() / 2, Constants.PPM)
                    .categoryBits(CategoryBits.PORTAL)
                    .isSensor(true)
                    .setUserData(portal)
                    .buildFixture();
        }
    }

    private static void createLightSources(RayHandler rayHandler, GameMap gameMap, GameMapLayer layer) {
        MapObjects mapObjects = gameMap.getTiledMap().getLayers().get(layer.ordinal()).getObjects();

        for (RectangleMapObject o : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = o.getRectangle();
            float x = rect.getX() + rect.getWidth() / 2;
            float y = rect.getY() + rect.getHeight() / 2;
            LightBuilder.createPointLight(rayHandler, LIGHT_COLOR, LIGHT_DISTANCE, x, y, Constants.PPM);
        }
    }

}