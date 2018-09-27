package com.aesophor.medievania.ui.pausemenu;

import com.aesophor.medievania.entity.character.Player;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class Pane extends Table {

    protected Texture paneBackgroundTexture;
    protected AssetManager assets;
    protected MenuDialog menuDialog;
    protected Player player;

    public Pane(AssetManager assets, float x, float y) {
        this.assets = assets;
        setPosition(x, y);
        setFillParent(true);

        // The alignment of a pane is set to bottom left by default.
        // Call any of the top(), bottom(), left() and right() to override alignment.
        bottom().left();
    }

    public Pane(AssetManager assets, Player player, float x, float y) {
        this(assets, x, y);
        this.player = player;
    }

    public Pane(AssetManager assets, Player player, MenuDialog menuDialog, float x, float y) {
        this(assets, player, x, y);
        this.menuDialog = menuDialog;
    }


    public abstract void handleInput(float delta);

    public Texture getBackgroundTexture() {
        return  paneBackgroundTexture;
    }

}
