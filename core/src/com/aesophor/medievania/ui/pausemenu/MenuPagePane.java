package com.aesophor.medievania.ui.pausemenu;

import com.badlogic.gdx.graphics.Texture;

public interface MenuPagePane {

    public void handleInput(float delta);
    public Texture getBackgroundTexture();

}