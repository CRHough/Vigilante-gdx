package com.aesophor.medievania.map;

import com.badlogic.gdx.physics.box2d.Body;

public class Portal {

    private String targetMap;
    private int targetPortalID;
    private Body body;

    public Portal(String targetGameMap, int targetPortalID, Body body) {
        this.targetMap = targetGameMap;
        this.targetPortalID = targetPortalID;
        this.body = body;
    }


    public String getTargetMap() {
        return targetMap;
    }

    public int getTargetPortalID() {
        return targetPortalID;
    }

    public Body getBody() {
        return body;
    }


    @Override
    public String toString() {
        return targetMap;
    }

}