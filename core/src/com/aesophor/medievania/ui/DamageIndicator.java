package com.aesophor.medievania.ui;

import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DamageIndicator extends Stage {

    private BitmapFont font;
    private Camera gameScreenCamera;
    private float damageTextLifetime;

    public DamageIndicator(Batch batch, BitmapFont font, Camera gameScreenCamera, float damageTextLifetime) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), batch);

        this.font = font;
        this.gameScreenCamera = gameScreenCamera;
        this.damageTextLifetime = damageTextLifetime;
    }


    /**
     * Shows damage indicator above the specified character's head.
     * @param c target character.
     * @param damage amount of damage.
     */
    public void show(Character c, int damage) {
        // Move previous text indicator up.
        for (Actor i : c.getDamageIndicators()) {
            i.addAction(Actions.moveBy(0, 10f, .2f));
        }

        // Display the new message.
        // Rename OnScreenText later! It can be reused for displaying on-screens texts, so
        // the name should be more generic.
        TimedLabel indicator = new TimedLabel(Integer.toString(damage), new Label.LabelStyle(font, Color.WHITE), damageTextLifetime);

        // Convert the coordinate from world to screens.
        Vector3 worldCoordinates = new Vector3(c.getB2Body().getPosition().x, c.getB2Body().getPosition().y, 0);
        gameScreenCamera.project(worldCoordinates);

        indicator.setPosition(worldCoordinates.x * Constants.V_WIDTH / getViewport().getScreenWidth(), worldCoordinates.y * Constants.V_HEIGHT / getViewport().getScreenHeight());
        indicator.addAction(Actions.moveBy(0, 10f, .2f));
        c.getDamageIndicators().addLast(indicator);
        addActor(indicator);
    }


    public void update(float delta) {

        // Remove the actor from character's queue....
        // do this tomorrow i'm going to bed right now...
        for (Actor i : getActors()) {
            TimedLabel indicator = ((TimedLabel) i);
            indicator.update(delta);

            if (indicator.hasExpired()) {
                indicator.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.removeActor()));
            }
        }


        act(delta);
    }

}