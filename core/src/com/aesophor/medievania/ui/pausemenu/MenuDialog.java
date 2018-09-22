package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.event.GameEvent;
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

public class MenuDialog extends Table implements MenuPagePane {

    private class DialogOption extends HorizontalGroup {

        private final Image image;
        private final Label text;
        private GameEvent optionEvent;

        public DialogOption(Texture texture, String text) {
            this(texture, text, null);
        }

        public DialogOption(Texture texture, String text, GameEvent optionEvent) {
            this.image = new Image(texture);
            this.text = new Label(text, LabelStyles.WHITE_REGULAR);
            this.optionEvent = optionEvent;

            this.padRight(10f);
            this.addActor(this.image);
            this.addActor(this.text);

            this.setSelected(false);
        }


        public GameEvent getOptionEvent() {
            return optionEvent;
        }

        public void setOptionEvent(GameEvent optionEvent) {
            this.optionEvent = optionEvent;
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

    public MenuDialog(AssetManager assets) {
        Texture triangleTexture = assets.get("interface/triangle.png");

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
        optionTable.setPosition(-125, 0);

        options = new Array<>(2);
        options.add(new DialogOption(triangleTexture, ""));
        options.add(new DialogOption(triangleTexture, ""));
        options.first().setSelected(true);
        options.forEach(optionTable::add);

        addActor(optionTable);
        setVisible(false);
    }


    public void show(String message, String option1, String option2, GameEvent option1Event, GameEvent option2Event) {
        this.message.setText(message);
        this.options.get(0).setText(option1);
        this.options.get(1).setText(option2);
        this.options.get(0).setOptionEvent(option1Event);
        this.options.get(1).setOptionEvent(option2Event);
        setVisible(true);
    }

    public DialogOption getSelectedItem() {
        return options.get(currentItemIdx);
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
            setVisible(false);

            // Fire the user-specified event corresponding to user's choice.
            GameEvent selectedOptionEvent = getSelectedItem().getOptionEvent();
            if (selectedOptionEvent != null) {
                GameEventManager.getInstance().fireEvent(selectedOptionEvent);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setVisible(false);
        }
    }

    @Override
    public Texture getBackgroundTexture() {
        return null;
    }

}