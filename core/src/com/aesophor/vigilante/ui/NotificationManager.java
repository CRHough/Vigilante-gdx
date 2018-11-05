package com.aesophor.vigilante.ui;

import com.aesophor.vigilante.ui.component.Notification;
import com.aesophor.vigilante.util.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class NotificationManager extends Stage {

    private static final float NOTIFICATION_STARTING_X = 10f;
    private static final float NOTIFICATION_STARTING_Y = 0f;

    private static final float DELTA_X = 0f;
    private static final float DELTA_Y = 10f;

    private static final float MOVE_UP_DURATION = .2f;
    private static final float FADE_OUT_DURATION = 1f;

    private BitmapFont font;
    private int notificationQueueSize;
    private float notificationLifetime;

    public NotificationManager(Batch batch, BitmapFont font, int notificationQueueSize, float notificationLifetime) {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT), batch);

        this.font = font;
        this.notificationQueueSize = notificationQueueSize;
        this.notificationLifetime = notificationLifetime;
    }


    /**
     * Shows a notification.
     * @param content notification content.
     */
    public void show(String content) {
        // If the number of notifications being displayed has surpassed notificationQueueSize,
        // then remove the earliest notification.
        if (getActors().size > notificationQueueSize) {
            getActors().first().remove();
        }

        // Move previous notifications up.
        for (Actor message : getActors()) {
            message.addAction(Actions.moveBy(DELTA_X, DELTA_Y, MOVE_UP_DURATION));
        }

        // Display the new notification.
        Notification notification = new Notification(content, new Label.LabelStyle(font, Color.WHITE), notificationLifetime);
        notification.setPosition(NOTIFICATION_STARTING_X, NOTIFICATION_STARTING_Y);
        notification.addAction(Actions.moveBy(DELTA_X, DELTA_Y, MOVE_UP_DURATION));
        addActor(notification);
    }

    /**
     * Shows a notificatiion as a hint at the upper center of the screen.
     * @param content notification/hint content.
     */
    public void showAsHint(String content) {
        // TODO: Show as hint is still buggy...
        // Display the new notification.
        Notification notification = new Notification(content, new Label.LabelStyle(font, Color.WHITE), notificationLifetime);
        notification.setPosition(getWidth() / 2 - notification.getWidth() / 2, getHeight() / 2 + 50);
        addActor(notification);
    }


    public void update(float delta) {
        for (Actor actor : getActors()) {
            Notification notification = ((Notification) actor);
            notification.update(delta);

            // If any notification has expired, fade out from the stage and remove it from actors.
            if (notification.hasExpired()) {
                notification.addAction(Actions.sequence(Actions.fadeOut(FADE_OUT_DURATION), Actions.removeActor()));
            }
        }

        act(delta);
    }

}