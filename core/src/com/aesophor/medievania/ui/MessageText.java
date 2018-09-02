package com.aesophor.medievania.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MessageText extends Actor {

    private Skin skin;
    private Label text;
    private float deltaY;

    private float lifetime;
    private float fadeDuration;
    private long animationStartTime;

    public MessageText(Skin skin, String text, float lifetime, float fadeDuration, float deltaY) {
        this.skin = skin;
        this.text = new Label(text, skin);
        this.lifetime = lifetime;
        this.fadeDuration = fadeDuration;
        this.deltaY = deltaY;

        this.animationStartTime = System.currentTimeMillis();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isDisposable()) {
            dispose();
            return;
        } else {
            float elapsed = System.currentTimeMillis() - animationStartTime;

            // If the lifetime of this message has elapsed, start fading the text.
            if (elapsed >= lifetime) {
                //text.setPosition(getX(), getY() + deltaY * elapsed / 1000f);
                text.draw(batch, parentAlpha * (1 - elapsed / fadeDuration));
            } else {
                text.draw(batch, 1f);
            }
        }
    }

    private boolean isDisposable() {
        return System.currentTimeMillis() > animationStartTime + lifetime + fadeDuration;
    }

    private void dispose() {
        remove();
    }

}
