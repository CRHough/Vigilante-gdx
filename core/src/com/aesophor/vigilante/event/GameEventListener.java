package com.aesophor.vigilante.event;

public interface GameEventListener<T extends GameEvent> {

    /**
     * Handles the specified GameEvent. Use an anonymous class and override the
     * handle(T e) to specify exactly what you want to do upon the event's
     * arrival. Or alternatively, use lambda expression for shorter code.
     * @param e the Game Event to handle.
     */
    public void handle(T e);

}