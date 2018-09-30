package com.aesophor.medievania.ui;

import com.aesophor.medievania.GameAssetManager;
import com.aesophor.medievania.GameStateManager;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.event.ui.DialogEndedEvent;
import com.aesophor.medievania.event.ui.DialogStartedEvent;
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

    private static final float SHOW_NEXT_CHARACTER_INTERVAL = .05f;

    private Array<Dialog> pendingDialogs;
    private Dialog currentDialog;
    private float timer;

    private final Label currentSpeakerName;
    private final Label message;
    private final Table dialogTable;

    public DialogBox(GameStateManager gsm) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());

        Texture background = gsm.getAssets().get(GameAssetManager.MESSAGE_BOX_BG);

        pendingDialogs = new Array<>();

        currentSpeakerName = new Label("", LabelStyles.WHITE_HEADER);
        message = new Label("", LabelStyles.WHITE_REGULAR);
        message.setWrap(true);

        dialogTable = new Table();
        dialogTable.top().left();
        dialogTable.setPosition(getWidth() / 2 - background.getWidth() / 2, getHeight() / 2 - background.getHeight() / 2 + 75f);
        dialogTable.setSize(background.getWidth(), background.getHeight());

        dialogTable.defaults().padLeft(10f).spaceBottom(3f).left();
        dialogTable.background(new TextureRegionDrawable(new TextureRegion(background)));
        dialogTable.add(currentSpeakerName).padTop(5f).spaceBottom(5f).row();
        dialogTable.add(message).width(background.getWidth() - 20f);
        dialogTable.setVisible(false);

        addActor(dialogTable);
    }


    public void show(Array<Dialog> dialogMessages) {
        pendingDialogs.addAll(dialogMessages);
        dialogTable.setVisible(true);
        showNext();

        GameEventManager.getInstance().fireEvent(new DialogStartedEvent());
    }

    private void showNext() {
        currentDialog = pendingDialogs.first();
        pendingDialogs.removeIndex(0);

        currentSpeakerName.setText(currentDialog.getSpeakerName());
        message.setText("");
    }

    private boolean hasDialogFinishedShowing() {
        return message.getText().length == currentDialog.getDialog().length();
    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (pendingDialogs.size > 0) {
                showNext();
            } else {
                dialogTable.setVisible(false);
                GameEventManager.getInstance().fireEvent(new DialogEndedEvent());
                // Maybe fire a event here...?
            }
        }
    }

    public void update(float delta) {
        if (dialogTable.isVisible()) {
            if (hasDialogFinishedShowing()) {
                handleInput(delta);
            } else {
                if (timer >= SHOW_NEXT_CHARACTER_INTERVAL) {
                    int nextCharIdx = message.getText().length;
                    message.setText(message.getText().toString() + currentDialog.getDialog().charAt(nextCharIdx));
                    timer = 0;
                }

                timer += delta;
            }
        }

        act(delta);
    }

}