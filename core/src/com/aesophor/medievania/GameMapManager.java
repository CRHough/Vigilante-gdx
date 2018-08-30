package com.aesophor.medievania;

import box2dLight.RayHandler;
import com.aesophor.medievania.character.Player;
import com.aesophor.medievania.map.GameMap;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public interface GameMapManager extends Disposable {

    public void load(String gameMapFile);
    public AssetManager getAssets();
    public RayHandler getRayHandler();
    public TmxMapLoader getMapLoader();
    public World getWorld();
    public GameMap getCurrentMap();
    public Player getPlayer();

}
