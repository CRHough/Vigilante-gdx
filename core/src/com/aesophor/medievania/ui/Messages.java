package com.aesophor.medievania.ui;

import com.aesophor.medievania.manager.GameStateManager;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.concurrent.TimeUnit;

public class Messages extends Stage {

    private static final String SKIN_FILE = "Interface/Skin/medievania_skin.json";

    private GameStateManager gsm;
    private Skin skin;
    private Label text;
    private float animationDuration;
    private float deltaX;
    private float deltaY;
    private boolean animated;
    private long animationStartTime;

    private MessageText messageText;

    public Messages(GameStateManager gsm, float animationDuration, float deltaX, float deltaY) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;
        skin = gsm.getAssets().get(SKIN_FILE);

        this.animationDuration = animationDuration;
        this.deltaX = deltaX;
        this.deltaY = deltaY;


        //messageText = new MessageText(skin,"Earned 25 exp.", TimeUnit.SECONDS.toMillis(2), TimeUnit.SECONDS.toMillis(3), 10);
        text = new Label("You earned 25 exp.", skin);
        text.setPosition(10f, 10f);
        addActor(text);

        //text.addAction(Actions.moveBy(0, 10, 2));
        //messageText.addAction(Actions.fadeOut(2f));
    }


    public void update(float delta) {
        act(delta);
    }

}
