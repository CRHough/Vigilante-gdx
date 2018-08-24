package com.aesophor.medievania.util;

import com.aesophor.medievania.constant.Constants;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraHelper {
    
    private CameraHelper() {
        
    }

    /**
     * Bounds the camera within the specified TiledMap.
     * @param camera
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public static void boundCamera(Camera camera, TiledMap map) {
        Vector3 position = camera.position;
        float startX = camera.viewportWidth / 2;
        float startY = camera.viewportHeight / 2;
        float endX =  (map.getProperties().get("width", Integer.class) * 16) / Constants.PPM - camera.viewportWidth / 2;
        float endY = (map.getProperties().get("height", Integer.class) * 16) / Constants.PPM - camera.viewportHeight / 2;
        
        if (position.x < startX) {
            position.x = startX;
        }
        if (position.y < startY) {
            position.y = startY;
        }
        
        if (position.x > endX) {
            position.x = endX;
        }
        if (position.y > endY) {
            position.y = endY;
        }
        
        camera.position.set(position);
        camera.update();
    }
    
    public static void lerpToTarget(Camera camera, Vector2 target) {
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * .1f;
        position.y = camera.position.y + (target.y - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }
    
}
