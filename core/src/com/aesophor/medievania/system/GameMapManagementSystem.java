package com.aesophor.medievania.system;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.entity.item.equipment.Axe;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.combat.EnemyDiedEvent;
import com.aesophor.medievania.event.combat.ItemPickedUpEvent;
import com.aesophor.medievania.event.map.MapChangedEvent;
import com.aesophor.medievania.event.map.PortalUsedEvent;
import com.aesophor.medievania.map.GameMap;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class GameMapManagementSystem extends EntitySystem {

    private PooledEngine engine;
    private AssetManager assets;
    private World world;
    private GameMap currentMap;

    private Player player;
    private Array<Character> npcs;

    public GameMapManagementSystem(PooledEngine engine, AssetManager assets, World world) {
        this.engine = engine;
        this.assets = assets;
        this.world = world;

        npcs = new Array<>();

        GameEventManager.getInstance().addEventListener(GameEventType.PORTAL_USED, (PortalUsedEvent e) -> {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    setGameMap(e.getPortal().getTargetMap());

                    int targetPortalID = e.getPortal().getTargetPortalID();
                    player.reposition(currentMap.getPortals().get(targetPortalID).getBody().getPosition());
                }
            }, ScreenFadeSystem.FADEIN_DURATION);
        });

        GameEventManager.getInstance().addEventListener(GameEventType.ENEMY_DIED, (EnemyDiedEvent e) -> {
            // Clean up this part later...
            Item item = new Axe(assets.get("item/RusticAxe.png"), world, e.getEnemy().getB2Body().getPosition().x * 100, e.getEnemy().getB2Body().getPosition().y * 100);
            Body body = Mappers.B2BODY.get(item).getBody();
            body.applyLinearImpulse(new Vector2(0, 2.5f), body.getWorldCenter(), true);
            engine.addEntity(item);
        });

        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_PICKED_UP, (ItemPickedUpEvent e) -> {
            engine.removeEntity(e.getItem());
        });

        setGameMap("map/starting_point.tmx");
    }

    public void registerPlayer(Player player) {
        this.player = player;
    }


    /**
     * Sets the speicified GameMap as the current one.
     * @param gameMapFile path to the .tmx tiled map.
     */
    public void setGameMap(String gameMapFile) {
        engine.clearPools();

        // Dispose previous map data if there is any.
        if (currentMap != null) {
            // Stop the background music, lights and dispose previous GameMap.
            currentMap.getBackgroundMusic().stop();
            currentMap.dispose();

            // Destroy all bodies except player's body.
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);

            for(int i = 0; i < bodies.size; i++)
            {
                if (!bodies.get(i).equals(player.getB2Body())) {
                    world.destroyBody(bodies.get(i));
                }
            }

            // Ashley's engine.getEntitiesFor() is broken :( ...
            for (Character c : npcs) {
                engine.removeEntity(c);
            }
        }

        // Load the new map from gameMapFile.
        currentMap = new GameMap(assets, world, gameMapFile);
        currentMap.playBackgroundMusic();

        GameEventManager.getInstance().fireEvent(new MapChangedEvent(currentMap));

        // TODO: Don't respawn enemies whenever a map loads.
        npcs = currentMap.spawnNPCs();
        npcs.forEach(engine::addEntity);
    }

}