package com.aesophor.medievania.ui;

import com.aesophor.medievania.util.Font;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelStyles {

    public static final Label.LabelStyle WHITE;
    public static final Label.LabelStyle GRAY;
    public static final Label.LabelStyle RED;

    static {
        WHITE = new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE);
        GRAY = new Label.LabelStyle(Font.getDefaultFont(), Color.GRAY);
        RED = new Label.LabelStyle(Font.getDefaultFont(), Color.MAROON);
    }

}