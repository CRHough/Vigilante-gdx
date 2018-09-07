package com.aesophor.medievania.ui;

import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class NotificationArea extends Stage {

    private BitmapFont font;
    private int messageQueueSize;
    private float messageLifetime;

    public NotificationArea(Batch batch, BitmapFont font, int messageQueueSize, float messageLifetime) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), batch);

        this.font = font;
        this.messageQueueSize = messageQueueSize;
        this.messageLifetime = messageLifetime;
    }

    public void show(String content) {
        if (getActors().size > messageQueueSize) {
            getActors().get(0).remove();
        }

        // Move previous messages up.
        for (Actor message : getActors()) {
            message.addAction(Actions.moveBy(0, 10f, .2f));
        }

        // Display the new message.
        // Rename TimedLabel later! It can be reused for displaying on-screens texts, so
        // the name should be more generic.
        TimedLabel newMsg = new TimedLabel(content, new Label.LabelStyle(font, Color.WHITE), messageLifetime);
        newMsg.setPosition(10f, 0f);
        newMsg.addAction(Actions.moveBy(0f, 10f, .2f));
        addActor(newMsg);
    }


    public void update(float delta) {
        // If any previous message has expired
        for (Actor message : getActors()) {
            TimedLabel m = ((TimedLabel) message);
            m.update(delta);

            if (m.hasExpired()) {
                m.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.removeActor()));
            }
        }

        act(delta);
    }

}
