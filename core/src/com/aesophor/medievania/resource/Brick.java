package com.aesophor.medievania.resource;

import com.aesophor.medievania.constant.Constants;
import com.aesophor.medievania.world.object.InteractiveTileObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Brick extends InteractiveTileObject {
    
    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Constants.BRICK_BIT);
    }

    
    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "collision");
        setCategoryFilter(Constants.DESTROYED_BIT);
        getCell().setTile(null);
    }
    
}