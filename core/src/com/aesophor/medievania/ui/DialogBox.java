package com.aesophor.medievania.ui;

import com.aesophor.medievania.GameAssetManager;
import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DialogBox extends Stage {

    private Label currentSpeakerName;
    private Label message;
    private Table messageBoxTable;
    private Array<Dialog> pendingMessages;

    public DialogBox(GameStateManager gsm) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());

        Texture background = gsm.getAssets().get(GameAssetManager.MESSAGE_BOX_BG);

        currentSpeakerName = new Label("", LabelStyles.WHITE_HEADER);
        message = new Label("", LabelStyles.WHITE_REGULAR);
        message.setWrap(true);

        pendingMessages = new Array<>();

        messageBoxTable = new Table();
        messageBoxTable.top().left();
        messageBoxTable.setPosition(getWidth() / 2 - background.getWidth() / 2, getHeight() / 2 - background.getHeight() / 2 + 75f);
        messageBoxTable.setSize(background.getWidth(), background.getHeight());

        messageBoxTable.defaults().padLeft(10f).spaceBottom(3f).left();
        messageBoxTable.background(new TextureRegionDrawable(new TextureRegion(background)));
        messageBoxTable.add(currentSpeakerName).padTop(5f).spaceBottom(5f).row();
        messageBoxTable.add(message).width(background.getWidth() - 20f);
        messageBoxTable.setVisible(false);

        addActor(messageBoxTable);
    }


    public void show(Array<Dialog> dialogMessages) {
        pendingMessages.addAll(dialogMessages);
        messageBoxTable.setVisible(true);
        showNext();
    }

    private void showNext() {
        Dialog nextMessage = pendingMessages.first();
        pendingMessages.removeIndex(0);

        currentSpeakerName.setText(nextMessage.getSpeakerName());
        message.setText(nextMessage.getMessage());
    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (pendingMessages.size > 0) {
                showNext();
            } else {
                messageBoxTable.setVisible(false);
                // Maybe fire a event here...?
            }
        }
    }

    public void update(float delta) {
        if (messageBoxTable.isVisible()) {
            handleInput(delta);
            act(delta);
        }
    }

}