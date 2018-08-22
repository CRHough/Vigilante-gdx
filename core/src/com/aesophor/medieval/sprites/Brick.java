package com.aesophor.medieval.sprites;

import com.aesophor.medieval.Medieval;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Brick extends InteractiveTileObject {
    
    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Medieval.BRICK_BIT);
    }

    
    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "collision");
        setCategoryFilter(Medieval.DESTROYED_BIT);
        getCell().setTile(null);
    }
    
}