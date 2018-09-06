package com.aesophor.medievania.component;

import com.aesophor.medievania.util.box2d.BodyBuilder;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class B2BodyComponent implements Component {

    public BodyBuilder bodyBuilder;
    public Body body;
    public Fixture bodyFixture;
    public Fixture feetFixture;
    public Fixture meleeWeaponFixture;

    public B2BodyComponent(World world) {
        bodyBuilder = new BodyBuilder(world);
    }

}