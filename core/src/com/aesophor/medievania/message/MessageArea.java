package com.aesophor.medievania.message;

import com.aesophor.medievania.manager.GameStateManager;
import com.aesophor.medievania.util.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MessageArea extends Stage {

    private GameStateManager gsm;
    private Queue<Message> messages;
    private float messageLifetime;

    public MessageArea(GameStateManager gsm, int messageQueueSize, float messageLifetime) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), gsm.getBatch());
        this.gsm = gsm;

        this.messages = new Queue<>(messageQueueSize);
        this.messageLifetime = messageLifetime;
    }

    public void show(String content) {
        for (Message m : messages) {
            m.addAction(Actions.moveBy(0, 10f, .2f));
        }

        Message newMsg = new Message(content, new Label.LabelStyle(gsm.getFont().getDefaultFont(), Color.WHITE), messageLifetime);
        newMsg.addAction(Actions.moveBy(0f, 10f, .2f));
        messages.addLast(newMsg);
        addActor(newMsg);
    }


    public void update(float delta) {
        for (Message m : messages) {
            m.update(delta);

            if (m.hasExpired()) {
                m.addAction(Actions.fadeOut(1f));
            }
        }

        act(delta);
    }

}
