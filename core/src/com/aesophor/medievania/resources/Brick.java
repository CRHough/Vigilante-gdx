package com.aesophor.medievania.resources;

import com.aesophor.medievania.Medievania;
import com.aesophor.medievania.world.objects.InteractiveTileObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Brick extends InteractiveTileObject {
    
    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Medievania.BRICK_BIT);
    }

    
    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "collision");
        setCategoryFilter(Medievania.DESTROYED_BIT);
        getCell().setTile(null);
    }
    
}