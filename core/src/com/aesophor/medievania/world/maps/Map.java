package com.aesophor.medievania.world.maps;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.resources.Brick;
import com.aesophor.medievania.resources.Coin;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Map {
    
    public static final int GROUND = 0;
    
    public Map(World world, TiledMap map) {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        
        
        // Create ground bodies/Fixtures.
        for (MapObject object : map.getLayers().get(GROUND).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Medievania.PPM, (rect.getY() + rect.getHeight() / 2) / Medievania.PPM);
            
            body = world.createBody(bdef);
            
            shape.setAsBox(rect.getWidth() / 2 / Medievania.PPM, rect.getHeight() / 2 / Medievania.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Medievania.GROUND_BIT;
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
        
    }

}
