package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.event.GameEvent;
import com.aesophor.medievania.event.GameEventListener;
import com.aesophor.medievania.event.GameEventManager;
import com.aesophor.medievania.ui.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class DialogTable extends Table implements MenuItemTable {

    private class DialogOption extends HorizontalGroup {

        private final Image image;
        private final Label text;
        private GameEvent confirmEvent;

        public DialogOption(Texture texture, String text) {
            this(texture, text, null);
        }

        public DialogOption(Texture texture, String text, GameEvent confirmEvent) {
            this.image = new Image(texture);
            this.text = new Label(text, LabelStyles.WHITE_REGULAR);
            this.confirmEvent = confirmEvent;

            this.padRight(10f);
            this.addActor(this.image);
            this.addActor(this.text);

            this.setSelected(false);
        }


        public GameEvent getConfirmEvent() {
            return confirmEvent;
        }

        public void setConfirmEvent(GameEvent confirmEvent) {
            this.confirmEvent = confirmEvent;
        }

        public void setText(String text) {
            this.text.setText(text);
        }

        public void setSelected(boolean selected) {
            image.setVisible(selected);
        }

    }

    private final Label message;
    private final Array<DialogOption> options;
    private int currentItemIdx;

    public DialogTable(AssetManager assets) {
        Texture triangleTexture = assets.get("interface/triangle.png");

        options = new Array<>(2);

        bottom().left();
        setPosition(55, 20);
        setFillParent(true);

        defaults().padRight(20f);
        message = new Label("", LabelStyles.WHITE_REGULAR);
        add(message);

        Table optionTable = new Table();
        optionTable.bottom().right();
        optionTable.setFillParent(true);
        optionTable.defaults().padRight(2f);
        options.add(new DialogOption(triangleTexture, ""));
        options.add(new DialogOption(triangleTexture, ""));
        options.first().setSelected(true);
        options.forEach(optionTable::add);
        optionTable.setPosition(-125 - optionTable.getWidth(), 0);

        this.addActor(optionTable);
    }


    public void show(String message, String option1, String option2, GameEvent confirmEvent) {
        this.message.setText(message);
        this.options.get(0).setConfirmEvent(confirmEvent);
        this.options.get(0).setText(option1);
        this.options.get(1).setText(option2);
        setVisible(true);
    }

    @Override
    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (currentItemIdx > 0) {
                options.get(currentItemIdx).setSelected(false);
                currentItemIdx--;
                options.get(currentItemIdx).setSelected(true);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (currentItemIdx < options.size - 1) {
                options.get(currentItemIdx).setSelected(false);
                currentItemIdx++;
                options.get(currentItemIdx).setSelected(true);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // This leaves some flexibility to add "cancel event" later (if I want to).
            options.forEach(option -> {
                if (option.getConfirmEvent() != null) {
                    GameEventManager.getInstance().fireEvent(option.getConfirmEvent());
                }
            });
            setVisible(false);
        }
    }

    @Override
    public Texture getBackgroundTexture() {
        return null;
    }

}