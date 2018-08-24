package com.aesophor.medievania.world.map;

import com.aesophor.medievania.constant.Constants;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Map {
    
    
    
    public Map(World world, TiledMap map) {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        
        
        // Create ground bodies/Fixtures.
        for (MapObject object : map.getLayers().get(Constants.GROUND_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            
            body = world.createBody(bdef);
            
            shape.setAsBox(rect.getWidth() / 2 / Constants.PPM, rect.getHeight() / 2 / Constants.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Constants.GROUND_BIT;
            body.createFixture(fdef);
        }
        /*
        // Create pipe bodies/Fixtures.
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Medieval.PPM, (rect.getY() + rect.getHeight() / 2) / Medieval.PPM);
            
            body = world.createBody(bdef);
            
            shape.setAsBox(rect.getWidth() / 2 / Medieval.PPM, rect.getHeight() / 2 / Medieval.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        
        // Create brick bodies/Fixtures.
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            new Brick(world, map, rect);
        }
        
        // Create coin bodies/Fixtures.
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            new Coin(world, map, rect);
        }
        */
        
        shape.dispose();
        
    }
    
    public static void parseTiledObjects(World world, MapObjects objects) {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        for (MapObject object : objects) {
            Shape shape;
            
            if (object instanceof PolylineMapObject) {
                shape = createPolyline((PolylineMapObject) object);
            } else {
                continue;
            }
            
            Body body;
            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            
            fdef.shape = shape;
            fdef.filter.categoryBits = Constants.GROUND_BIT;
            body.createFixture(fdef);
            
            shape.dispose();
        }
    }
    
    private static ChainShape createPolyline(PolylineMapObject object) {
        float[] vertices = object.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        
        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
        }
        
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }

}
