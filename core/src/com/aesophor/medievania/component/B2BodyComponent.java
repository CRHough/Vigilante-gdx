package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class B2BodyComponent implements Component {

    public Body b2body;

    public B2BodyComponent(Body b2body) {
        this.b2body = b2body;
    }

}