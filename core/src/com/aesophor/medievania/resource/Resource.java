package com.aesophor.medievania.resource;

import com.badlogic.gdx.utils.Disposable;

public class Resource implements Disposable {
    
    private static final Resource INSTANCE = new Resource();
    
    // HUD
    
    // Character Assets
    
    // Background Music
    
    // Sound Effects
    
    private Resource() {
        
    }
    
    public static Resource getInstance() {
        return INSTANCE;
    }

    @Override
    public void dispose() {
        
    }

    
}
