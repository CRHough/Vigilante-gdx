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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;
import java.util.Map;

public class DamageIndicatorFactory extends Stage {

    private BitmapFont font;
    private Camera gameScreenCamera;
    private float damageTextLifetime;
    private Map<Character, Array<DamageIndicator>> damageIndicators;

    public DamageIndicatorFactory(Batch batch, BitmapFont font, Camera gameScreenCamera, float damageTextLifetime) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), batch);

        this.font = font;
        this.gameScreenCamera = gameScreenCamera;
        this.damageTextLifetime = damageTextLifetime;
        this.damageIndicators = new HashMap<>();
    }


    /**
     * Shows damage indicator above the specified character's head.
     * @param c target character.
     * @param damage amount of damage.
     */
    public void show(Character c, int damage) {
        if (!damageIndicators.containsKey(c)) {
            damageIndicators.put(c, new Array<>());
        }

        // Move previous text indicator up.
        for (Actor i : damageIndicators.get(c)) {
            i.addAction(Actions.moveBy(0, 10f, .2f));
        }

        DamageIndicator indicator = new DamageIndicator(Integer.toString(damage), new Label.LabelStyle(font, Color.WHITE), damageTextLifetime, c);
        damageIndicators.get(c).add(indicator);

        // Convert the coordinate from world to screens.
        Vector3 worldCoordinates = new Vector3(c.getB2Body().getPosition().x, c.getB2Body().getPosition().y, 0);
        gameScreenCamera.project(worldCoordinates);

        indicator.setPosition(worldCoordinates.x * Constants.V_WIDTH / getViewport().getScreenWidth(), worldCoordinates.y * Constants.V_HEIGHT / getViewport().getScreenHeight());
        indicator.addAction(Actions.moveBy(0, 10f, .2f));
        addActor(indicator);
    }


    public void update(float delta) {
        for (Actor i : getActors()) {
            DamageIndicator indicator = ((DamageIndicator) i);
            indicator.update(delta);

            System.out.println(damageIndicators);

            if (indicator.hasExpired() && damageIndicators.get(indicator.getCharacter()) != null) {
                indicator.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.removeActor()));
                damageIndicators.get(indicator.getCharacter()).removeValue(indicator, true);

                if (damageIndicators.get(indicator.getCharacter()).size == 0) {
                    damageIndicators.remove(indicator.getCharacter());
                }
            }
        }

        act(delta);
    }

}