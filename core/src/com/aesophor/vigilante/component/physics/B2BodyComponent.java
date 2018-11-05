package com.aesophor.vigilante.component.physics;

import com.aesophor.vigilante.util.box2d.BodyBuilder;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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

    public World getWorld() {
        return body.getWorld();
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

    public Vector2 getFeetFixturePosition() {
        Vector2 bodyPos = body.getPosition();

        PolygonShape ps = (PolygonShape) feetFixture.getShape();
        Vector2 v1 = new Vector2();
        Vector2 v2 = new Vector2();
        ps.getVertex(0, v1); // (+, -)
        ps.getVertex(3, v2); // (-, -)

        v1.x = bodyPos.x + v1.x;
        v2.x = bodyPos.x + v2.x;
        float centerX = (v1.x + v2.x) / 2;

        return new Vector2(centerX, bodyPos.y + v1.y);
    }

}