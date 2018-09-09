package com.aesophor.medievania;

import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.ui.DamageIndicatorFactory;
import com.aesophor.medievania.ui.NotificationFactory;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public interface GameWorldManager extends Disposable {

    public World getWorld();
    public AssetManager getAssets();
    public TmxMapLoader getMapLoader();

    public NotificationFactory getNotificationFactory();
    public DamageIndicatorFactory getDamageIndicatorFactory();

    public PooledEngine getEngine();
    public Player getPlayer();

}
