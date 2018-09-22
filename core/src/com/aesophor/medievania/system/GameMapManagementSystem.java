package com.aesophor.medievania.system;

import com.aesophor.medievania.component.character.DroppableItemsComponent;
import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.physics.B2BodyComponent;
import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.GameEventType;
import com.aesophor.medievania.event.character.DiscardItemEvent;
import com.aesophor.medievania.event.combat.CharacterKilledEvent;
import com.aesophor.medievania.event.combat.ItemPickedUpEvent;
import com.aesophor.medievania.event.map.MapChangedEvent;
import com.aesophor.medievania.event.map.PortalUsedEvent;
import com.aesophor.medievania.event.screen.GamePausedEvent;
import com.aesophor.medievania.event.screen.GameResumedEvent;
import com.aesophor.medievania.map.GameMap;
import com.aesophor.medievania.util.Utils;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.lang.reflect.InvocationTargetException;

public class GameMapManagementSystem extends EntitySystem {

    private PooledEngine engine;
    private AssetManager assets;
    private GameMap currentMap;
    private World world;

    private Player player;
    private Array<Character> npcs;

    public GameMapManagementSystem(PooledEngine engine, AssetManager assets, World world) {
        this.engine = engine;
        this.assets = assets;
        this.world = world;

        npcs = new Array<>();

        // When the player used a portal, first we set the new (target) map as current map,
        // and then place the player's body at "the portal on the other side" which is another portal.
        GameEventManager.getInstance().addEventListener(GameEventType.PORTAL_USED, (PortalUsedEvent e) -> {
            // Set the new map until the fade in effect ends.
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    setGameMap(e.getPortal().getTargetMap());

                    int targetPortalID = e.getPortal().getTargetPortalID();
                    player.reposition(currentMap.getPortals().get(targetPortalID).getBody().getPosition());
                }
            }, ScreenFadeSystem.FADEIN_DURATION);
        });

        // When an enemy dies, enumerate through all of its droppable items and
        // decide whether the item should be dropped according to its drop rate.
        GameEventManager.getInstance().addEventListener(GameEventType.CHARACTER_KILLED, (CharacterKilledEvent e) -> {
            DroppableItemsComponent droppableItems = Mappers.DROP_ITEMS.get(e.getDeceased());
            B2BodyComponent deceasedB2Body = Mappers.B2BODY.get(e.getDeceased());

            droppableItems.getDroppableItems().forEach((itemName, dropRate) -> {
                if (Utils.randomInt(0, 100) / 100f <= dropRate) {
                    Item item = spawnItem(itemName, world, deceasedB2Body.getBody().getPosition().x, deceasedB2Body.getBody().getPosition().y);
                    Body body = Mappers.B2BODY.get(item).getBody();
                    body.applyLinearImpulse(new Vector2(0, 2.5f), body.getWorldCenter(), true);
                    engine.addEntity(item);
                }
            });
        });

        GameEventManager.getInstance().addEventListener(GameEventType.DISCARD_ITEM, (DiscardItemEvent e) -> {
            B2BodyComponent b2body = Mappers.B2BODY.get(player);

            Item item = e.getItem();
            item.constructBody();
            item.reloadTexture();

            Mappers.B2BODY.get(item).getBody().setTransform(b2body.getBody().getPosition().x, b2body.getBody().getPosition().y, 0);
            Body body = Mappers.B2BODY.get(item).getBody();
            body.applyLinearImpulse(new Vector2(0, 2.5f), body.getWorldCenter(), true);
            engine.addEntity(item);
        });

        // Remove the item entity from the engine once it has been picked up.
        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_PICKED_UP, (ItemPickedUpEvent e) -> {
            engine.removeEntity(e.getItem());
            e.getItem().dispose();
        });

        GameEventManager.getInstance().addEventListener(GameEventType.GAME_PAUSED, (GamePausedEvent e) -> {
            currentMap.getBackgroundMusic().pause();
        });

        GameEventManager.getInstance().addEventListener(GameEventType.GAME_RESUMED, (GameResumedEvent e) -> {
            currentMap.getBackgroundMusic().play();
        });

        setGameMap("map/starting_point.tmx");
    }

    public void registerPlayer(Player player) {
        this.player = player;
    }


    /**
     * Sets the specified GameMap as the current one.
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
                if (!bodies.get(i).equals(Mappers.B2BODY.get(player).getBody())) {
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

    private Item spawnItem(String itemName, World world, float x, float y) {
        Item item = null;

        try {
            item = (Item) Class.forName("com.aesophor.medievania.entity.item.Item")
                    .getConstructor(String.class, World.class, Float.class, Float.class)
                    .newInstance(itemName, world, x, y);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return item;
    }

}