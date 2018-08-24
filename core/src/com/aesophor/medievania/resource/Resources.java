package com.aesophor.medievania.resource;

import com.badlogic.gdx.utils.Disposable;

public class Resources implements Disposable {
    
    private static final Resources INSTANCE = new Resources();
    
    // HUD
    
    // Character Assets
    
    // Background Music
    
    // Sound Effects
    
    private Resources() {
        
    }
    
    public static Resources getInstance() {
        return INSTANCE;
    }

    @Override
    public void dispose() {
        
    }

    
}
