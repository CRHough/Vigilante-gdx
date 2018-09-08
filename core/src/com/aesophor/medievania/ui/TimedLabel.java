package com.aesophor.medievania.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class TimedLabel extends Label {

    protected float lifetime;
    protected float timer;

    public TimedLabel(CharSequence text, Skin skin, float lifetime) {
        super(text, skin);
        this.lifetime = lifetime;
    }

    public TimedLabel(CharSequence text, Skin skin, String styleName, float lifetime) {
        super(text, skin, styleName);
        this.lifetime = lifetime;
    }

    /** Creates a timed label, using a {@link LabelStyle} that has a BitmapFont with the specified name
     * from the skin and the specified color. */
    public TimedLabel(CharSequence text, Skin skin, String fontName, Color color, float lifetime) {
        super(text, skin, fontName, color);
        this.lifetime = lifetime;
    }

    /** Creates a timed label, using a {@link LabelStyle} that has a BitmapFont with the specified name
     * and the specified color from the skin. */
    public TimedLabel(CharSequence text, Skin skin, String fontName, String colorName, float lifetime) {
        super(text, skin, fontName, colorName);
        this.lifetime = lifetime;
    }

    public TimedLabel(CharSequence text, LabelStyle style, float lifetime) {
        super(text, style);
        this.lifetime = lifetime;
    }


    public void update(float delta) {
        this.timer += delta;
    }

    public boolean hasExpired() {
        return timer >= lifetime;
    }

}