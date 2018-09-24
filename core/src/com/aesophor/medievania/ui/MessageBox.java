package com.aesophor.medievania.ui;

import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MessageBox extends Stage {

    private final GameStateManager gsm;

    private Texture background;
    private Label currentSpeakerName;
    private Label message;

    public MessageBox(GameStateManager gsm) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;

        background = gsm.getAssets().get("interface/messagebox.png");

        currentSpeakerName = new Label("Dracula", LabelStyles.WHITE_HEADER);
        message = new Label("You pathetic mortal!", LabelStyles.WHITE_REGULAR);
        message.setWrap(true);

        Table messageBoxTable = new Table();
        messageBoxTable.bottom().left();
        messageBoxTable.setPosition(30, background.getHeight() - 20);
        messageBoxTable.setFillParent(true);

        messageBoxTable.defaults().padLeft(20f).spaceTop(5f).left();
        messageBoxTable.add(currentSpeakerName).row();
        messageBoxTable.add(message).width(background.getWidth() - 20f);

        addActor(messageBoxTable);
    }


    public void handleInput(float delta) {

    }

    public void update(float delta) {
        handleInput(delta);
        act(delta);

        gsm.getBatch().begin();
        gsm.getBatch().draw(background, 30, 20);
        gsm.getBatch().end();
    }

}