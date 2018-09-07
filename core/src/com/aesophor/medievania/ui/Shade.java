package com.aesophor.medievania.ui;

import com.aesophor.medievania.util.Constants;
import com.aesophor.medievania.util.Utils;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Shade extends Stage {

    private final Image shadeImage;

    public Shade(Batch batch) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), batch);

        // Initialize shadeImage to provide fade in/out effects later.
        // The shadeImage is drawn atop everything, with only its transparency being adjusted.
        shadeImage = new Image(new TextureRegion(Utils.getTexture()));
        shadeImage.setSize(getViewport().getScreenWidth(), getViewport().getScreenHeight());
        shadeImage.setColor(0, 0, 0, 0);
        addActor(shadeImage);
    }


    @Override
    public void addAction(Action action) {
        shadeImage.addAction(action);
    }

}