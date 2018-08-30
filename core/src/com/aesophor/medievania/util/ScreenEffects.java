package com.aesophor.medievania.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ScreenEffects {

    private Batch batch;
    private Camera camera;

    private Image shade;
    private float shadeAlpha;
    private float fadeDuration;
    private boolean fadeInFinished;
    private boolean fadeOutFinished;

    public ScreenEffects(Batch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;

        shade = new Image(new TextureRegion(Utils.getTexture()));
        shade.setColor(Color.BLACK);

        fadeInFinished = true;
        fadeOutFinished = true;
    }


    public void draw() {
        if (!fadeOutFinished) {
            shadeAlpha += (1f / 60f) / fadeDuration;

            if (shadeAlpha >= 1) {
                shadeAlpha = 1f;
                fadeOutFinished = true;
            }
        }

        if (fadeOutFinished && !fadeInFinished) {
            shadeAlpha -= (1f / 60f) / fadeDuration;

            if (shadeAlpha <= 0) {
                shadeAlpha = 0f;
                fadeInFinished = true;
            }
        }

        // Draws the shade over everything to provide fade in/out effect.
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        shade.draw(batch, shadeAlpha);
        batch.end();
    }

    /**
     * Fades out current screen into pure black within the specified duration.
     * @param duration fade out duration.
     */
    public void fadeOut(float duration) {
        fadeDuration = duration;
        shadeAlpha = 0f;
        fadeOutFinished = false;
    }

    /**
     * Fades in from pure black within the specified duration.
     * @param duration fade in duration.
     */
    public void fadeIn(float duration) {
        fadeDuration = duration;
        shadeAlpha = 1f;
        fadeInFinished = false;
    }

    /**
     * Updates the size of shade image. Must be called every time when
     * the current GameMap has changed.
     * @param width width of the shade. Same as the width of the map.
     * @param height height of the shade. Same as the height of the map.
     */
    public void updateShadeSize(float width, float height) {
        shade.setSize(width, height);
    }

}