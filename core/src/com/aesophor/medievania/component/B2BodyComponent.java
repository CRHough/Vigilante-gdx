package com.aesophor.medievania.component;

import com.aesophor.medievania.util.box2d.BodyBuilder;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class B2BodyComponent implements Component {

    private BodyBuilder bodyBuilder;
    private Body body;
    private Fixture bodyFixture;
    private Fixture feetFixture;
    private Fixture meleeWeaponFixture;

    public B2BodyComponent(World world) {
        bodyBuilder = new BodyBuilder(world);
    }


    public BodyBuilder getBodyBuilder() {
        return bodyBuilder;
    }

    public void setBodyBuilder(BodyBuilder bodyBuilder) {
        this.bodyBuilder = bodyBuilder;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Fixture getBodyFixture() {
        return bodyFixture;
    }

    public void setBodyFixture(Fixture bodyFixture) {
        this.bodyFixture = bodyFixture;
    }

    public Fixture getFeetFixture() {
        return feetFixture;
    }

    public void setFeetFixture(Fixture feetFixture) {
        this.feetFixture = feetFixture;
    }

    public Fixture getMeleeWeaponFixture() {
        return meleeWeaponFixture;
    }

    public void setMeleeWeaponFixture(Fixture meleeWeaponFixture) {
        this.meleeWeaponFixture = meleeWeaponFixture;
    }

}