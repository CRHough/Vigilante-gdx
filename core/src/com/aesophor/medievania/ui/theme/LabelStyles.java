package com.aesophor.medievania.ui.theme;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelStyles {

    public static final Label.LabelStyle WHITE_HEADER;
    public static final Label.LabelStyle GRAY_HEADER;

    public static final Label.LabelStyle WHITE_REGULAR;
    public static final Label.LabelStyle GRAY_REGULAR;
    public static final Label.LabelStyle RED_REGULAR;

    static {
        WHITE_HEADER = new Label.LabelStyle(Font.HEADER, Color.WHITE);
        GRAY_HEADER = new Label.LabelStyle(Font.HEADER, Colorscheme.GREY);

        WHITE_REGULAR = new Label.LabelStyle(Font.REGULAR, Color.WHITE);
        GRAY_REGULAR = new Label.LabelStyle(Font.REGULAR, Colorscheme.GREY);
        RED_REGULAR = new Label.LabelStyle(Font.REGULAR, Color.MAROON);
    }

}