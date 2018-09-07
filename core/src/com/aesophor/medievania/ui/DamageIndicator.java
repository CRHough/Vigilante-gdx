package com.aesophor.medievania.ui;

import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class DamageIndicator {

    private Stage mainGameStage;
    private BitmapFont font;
    private float damageTextLifetime;
    private Map<Character, Array<TimedLabel>> timedLabels;

    public DamageIndicator(Stage mainGameStage, BitmapFont font, float damageTextLifetime) {
        this.mainGameStage = mainGameStage;
        this.font = font;
        this.damageTextLifetime = damageTextLifetime;
        timedLabels = new HashMap<>();
    }


    /**
     * Shows damage indicator above the specified character's head.
     * @param c target character.
     * @param damage amount of damage.
     */
    public void show(Character c, int damage) {
        if (!timedLabels.containsKey(c)) {
            timedLabels.put(c, new Array<>());
        }

        // Move previous text indicator up.
        for (Actor i : timedLabels.get(c)) {
            i.addAction(Actions.moveBy(0, font.getLineHeight(), .2f));
        }

        TimedLabel indicator = new TimedLabel(Integer.toString(damage), new Label.LabelStyle(font, Color.WHITE), damageTextLifetime);
        timedLabels.get(c).add(indicator);
        // Convert the coordinate from world to screens.
        //Vector3 worldCoordinates = new Vector3(, , 0);
        //gameScreenCamera.project(worldCoordinates);
        indicator.setPosition(c.getB2Body().getPosition().x * 0, c.getB2Body().getPosition().y * 0 + 80);
        indicator.addAction(Actions.moveBy(0, font.getLineHeight(), .2f));
        System.out.println(indicator.getY());
        mainGameStage.addActor(indicator);
    }


    public void update(float delta) {
        // Remove the actor from character's queue....
        // do this tomorrow i'm going to bed right now...
        for (Actor i : mainGameStage.getActors()) {
            TimedLabel indicator = ((TimedLabel) i);
            indicator.update(delta);

            if (indicator.hasExpired()) {
                indicator.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.removeActor()));
            }
        }
    }

}