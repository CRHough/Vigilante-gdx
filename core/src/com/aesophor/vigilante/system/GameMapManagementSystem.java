package com.aesophor.vigilante.system;

import com.aesophor.vigilante.component.character.DroppableItemsComponent;
import com.aesophor.vigilante.component.Mappers;
import com.aesophor.vigilante.component.physics.B2BodyComponent;
import com.aesophor.vigilante.entity.character.Character;
import com.aesophor.vigilante.entity.character.Player;
import com.aesophor.vigilante.entity.item.Item;
import com.aesophor.vigilante.event.GameEventManager;
import com.aesophor.vigilante.event.GameEventType;
import com.aesophor.vigilante.event.character.ItemDiscardedEvent;
import com.aesophor.vigilante.event.combat.CharacterKilledEvent;
import com.aesophor.vigilante.event.character.ItemPickedUpEvent;
import com.aesophor.vigilante.event.map.MapChangedEvent;
import com.aesophor.vigilante.event.map.PortalUsedEvent;
import com.aesophor.vigilante.event.screen.GamePausedEvent;
import com.aesophor.vigilante.event.screen.GameResumedEvent;
import com.aesophor.vigilante.map.GameMap;
import com.aesophor.vigilante.system.ui.ScreenFadeSystem;
import com.aesophor.vigilante.util.Utils;
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

        this.npcs = new Array<>();

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

        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_DISCARDED, (ItemDiscardedEvent e) -> {
            B2BodyComponent b2body = Mappers.B2BODY.get(player);
            Item item = e.getItem();

            // The item's b2body and texture are destroyed/unloaded upon being picked up.
            // So here we have to reconstruct the b2body and reload its texture.
            item.constructIconBody();
            item.reloadIconTexture();

            Mappers.B2BODY.get(item).getBody().setTransform(b2body.getBody().getPosition().x, b2body.getBody().getPosition().y, 0);
            Body body = Mappers.B2BODY.get(item).getBody();
            body.applyLinearImpulse(new Vector2(0, 2.5f), body.getWorldCenter(), true);
            engine.addEntity(item);
        });

        // Remove the item entity from the engine once it has been picked up.
        GameEventManager.getInstance().addEventListener(GameEventType.ITEM_PICKED_UP, (ItemPickedUpEvent e) -> {
            // Remove the item entity from engine so that it will no longer be rendered by StaticSpriteRendererSystem.
            engine.removeEntity(e.getItem());

            // Now destroy the item's b2body and unload the texture.
            world.destroyBody(Mappers.B2BODY.get(e.getItem()).getBody());
            assets.unload(Mappers.ITEM_DATA.get(e.getItem()).getImage());
        });

        GameEventManager.getInstance().addEventListener(GameEventType.GAME_PAUSED, (GamePausedEvent e) -> {
            currentMap.getBackgroundMusic().pause();
        });

        GameEventManager.getInstance().addEventListener(GameEventType.GAME_RESUMED, (GameResumedEvent e) -> {
            currentMap.getBackgroundMusic().play();
        });

        setGameMap("Map/starting_point.tmx");


        Item item = new Item("Rusty Axe", assets, world, 1f, 1f);
        engine.addEntity(item);
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
            item = (Item) Class.forName("com.aesophor.vigilante.entity.item.Item")
                    .getConstructor(String.class, AssetManager.class, World.class, Float.class, Float.class)
                    .newInstance(itemName, assets, world, x, y);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return item;
    }

    @Override
    public void update(float delta) {

    }

}