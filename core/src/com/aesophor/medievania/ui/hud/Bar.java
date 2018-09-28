package com.aesophor.medievania.ui.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bar extends HorizontalGroup {

    private final Image padLeftImage;
    private final Image padRightImage;
    private final Image barImage;
    private float maxLength;

    public Bar(TextureRegion padLeft, TextureRegion padRight, TextureRegion bar, float maxLength) {
        this.padLeftImage = new Image(padLeft);
        this.padRightImage = new Image(padRight);
        this.barImage = new Image(bar);
        this.maxLength = maxLength;

        barImage.setScaleX(maxLength);

        addActor(padLeftImage);
        addActor(barImage);
        addActor(padRightImage);
    }

    public void update(int currentValue, int fullValue) {
        barImage.setScaleX(maxLength * currentValue / fullValue);
        padRightImage.setX(barImage.getX() + maxLength * currentValue / fullValue);
    }

}