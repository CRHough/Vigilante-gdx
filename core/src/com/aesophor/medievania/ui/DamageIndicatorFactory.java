package com.aesophor.medievania.ui;

import java.util.Map;
import java.util.HashMap;
import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.entity.character.Player;
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

public class DamageIndicatorFactory extends Stage {

    private static final float DELTA_X = 0f;
    private static final float DELTA_Y = 10f;

    private static final float MOVE_UP_DURATION = .2f;
    private static final float FADE_OUT_DURATION = .2f;

    private BitmapFont font;
    private Camera gameScreenCamera;
    private float damageTextLifetime;
    private Map<Character, Array<DamageIndicator>> damageIndicators;

    public DamageIndicatorFactory(Batch batch, BitmapFont font, Camera gameScreenCamera, float damageIndicatorLifetime) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), batch);

        this.font = font;
        this.gameScreenCamera = gameScreenCamera;
        this.damageTextLifetime = damageIndicatorLifetime;
        this.damageIndicators = new HashMap<>();
    }


    public void show(Character c, int damage) {
        // If the damageIndicators HashMap does not have an array of DamageIndicator for this character,
        // then initialize it.
        if (!damageIndicators.containsKey(c)) {
            damageIndicators.put(c, new Array<>());
        }

        // Move previous damage indicator owned by this character up.
        for (Actor i : damageIndicators.get(c)) {
            i.addAction(Actions.moveBy(DELTA_X, DELTA_Y, MOVE_UP_DURATION));
        }

        // Convert the coordinate from world to screen.
        Vector3 worldCoordinates = new Vector3(c.getB2Body().getPosition().x, c.getB2Body().getPosition().y, 0);
        Vector3 screenCoordinates = gameScreenCamera.project(worldCoordinates);

        // Display the new damage indicator.
        Color color = (c instanceof Player) ? Color.RED : Color.WHITE;
        DamageIndicator indicator = new DamageIndicator(Integer.toString(damage), new Label.LabelStyle(font, color), damageTextLifetime, c);
        float positionX = screenCoordinates.x * Constants.V_WIDTH / getViewport().getScreenWidth();
        float positionY = screenCoordinates.y * Constants.V_HEIGHT / getViewport().getScreenHeight();
        indicator.setPosition(positionX, positionY);
        indicator.addAction(Actions.moveBy(DELTA_X, DELTA_Y, MOVE_UP_DURATION));
        damageIndicators.get(c).add(indicator);
        addActor(indicator);
    }


    public void update(float delta) {
        for (Actor actor : getActors()) {
            DamageIndicator damageIndicator = ((DamageIndicator) actor);
            damageIndicator.update(delta);

            // If any damage indicator has expired, fade out from the stage and remove it from actors.
            // Also remove it from the character's array inside damageIndicators HashMap.
            if (damageIndicator.hasExpired() && damageIndicators.get(damageIndicator.getCharacter()) != null) {
                damageIndicator.addAction(Actions.sequence(Actions.fadeOut(FADE_OUT_DURATION), Actions.removeActor()));
                damageIndicators.get(damageIndicator.getCharacter()).removeValue(damageIndicator, true);

                // If there's no damage indicators left for this character,
                // then remove it from the HashMap.
                if (damageIndicators.get(damageIndicator.getCharacter()).size == 0) {
                    damageIndicators.remove(damageIndicator.getCharacter());
                }
            }
        }

        act(delta);
    }

}