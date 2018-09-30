package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.GameAssetManager;
import com.aesophor.medievania.event.GameEventListener;
import com.aesophor.medievania.event.ui.MenuDialogOptionEvent;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class MenuDialog extends Pane {

    private class DialogOption extends HorizontalGroup {

        private final Image image;
        private final Label text;
        private GameEventListener<MenuDialogOptionEvent> optionEvLstnr;

        public DialogOption(Texture texture, String text) {
            this(texture, text, null);
        }

        public DialogOption(Texture texture, String text, GameEventListener<MenuDialogOptionEvent> optionEvLstnr) {
            this.image = new Image(texture);
            this.text = new Label(text, LabelStyles.WHITE_REGULAR);
            this.optionEvLstnr = optionEvLstnr;

            this.padRight(10f);
            this.addActor(this.image);
            this.addActor(this.text);

            this.setSelected(false);
        }


        public GameEventListener<MenuDialogOptionEvent> getOptionEvLstnr() {
            return optionEvLstnr;
        }

        public void setOptionEvLstnr(GameEventListener<MenuDialogOptionEvent> optionEvLstnr) {
            this.optionEvLstnr = optionEvLstnr;
        }

        public void setText(String text) {
            this.text.setText(text);
        }

        public void setSelected(boolean selected) {
            image.setVisible(selected);
        }

    }

    private static final float ITEM_PAD_RIGHT = 3f;

    private final Label message;
    private final Array<DialogOption> options;
    private int currentItemIdx;

    public MenuDialog(AssetManager assets, float x, float y) {
        super(assets, x, y);

        // Initialize assets.
        Texture triangleTexture = assets.get(GameAssetManager.TRIANGLE);

        bottom().right();
        defaults().padRight(ITEM_PAD_RIGHT);
        message = new Label("", LabelStyles.WHITE_REGULAR);
        add(message);

        options = new Array<>(2);
        options.add(new DialogOption(triangleTexture, ""));
        options.add(new DialogOption(triangleTexture, ""));
        options.first().setSelected(true);
        options.forEach(this::add);

        // Hide the menu dialog by default.
        setVisible(false);
    }


    public void show(String message, String option1, String option2, GameEventListener<MenuDialogOptionEvent> option1EvLstnr, GameEventListener<MenuDialogOptionEvent> option2EvLstnr) {
        this.message.setText(message);
        this.options.get(0).setText(option1);
        this.options.get(1).setText(option2);
        this.options.get(0).setOptionEvLstnr(option1EvLstnr);
        this.options.get(1).setOptionEvLstnr(option2EvLstnr);
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

            // Handle the GameEventListener<MenuDialogOptionEvent> here.
            GameEventListener<MenuDialogOptionEvent> selectedOptionEvLstnr = getSelectedItem().getOptionEvLstnr();

            if (selectedOptionEvLstnr != null) {
                selectedOptionEvLstnr.handle(new MenuDialogOptionEvent() {});
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setVisible(false);
        }
    }

}