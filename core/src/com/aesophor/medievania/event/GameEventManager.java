package com.aesophor.medievania.event;

import java.util.Map;
import java.util.HashMap;
import com.badlogic.gdx.utils.Array;

public class GameEventManager {

    private static GameEventManager INSTANCE;

    private final Map<GameEventType, Array<GameEventListener>> listeners;

    private GameEventManager() {
        listeners = new HashMap<>();

        // List of handlers for various events.
        listeners.put(GameEventType.MAP_CHANGED, new Array<>());
        listeners.put(GameEventType.PORTAL_USED, new Array<>());
        listeners.put(GameEventType.MAINGAME_SCREEN_RESIZED, new Array<>());
    }

    public static GameEventManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameEventManager();
        }
        return INSTANCE;
    }


    /**
     * Adds a GameEventListeners which subscribes to a specific type of Event.
     * @param gameEventType the type of the Event which the handler will handle.
     * @param handler the EventHandler to be added.
     */
    public void addEventListener(GameEventType gameEventType, GameEventListener<? extends GameEvent> handler) {
        this.listeners.get(gameEventType).add(handler);
    }

    /**
     * Clears all GameEventListeners recorded in the handlers HashMap.
     * This should be called upon user logging out.
     */
    public void clearEventListeners() {
        for (Array<GameEventListener> listeners : listeners.values()) {
            listeners.clear();
        }
    }

    /**
     * Fires the specified GameEvents to all of its GameEventListeners.
     * @param gameEvent the GameEvent to fire.
     */
    public void fireEvent(GameEvent gameEvent) {
        GameEventType gameEventType = gameEvent.getGameEventType();

        for (GameEventListener<? super GameEvent> listener : this.listeners.get(gameEventType)) {
            listener.handle(gameEvent);
        }
    }

}