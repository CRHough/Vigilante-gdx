package com.aesophor.medievania.screen;

import com.aesophor.medievania.constants.Constants;
import com.aesophor.medievania.util.CameraUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen extends AbstractScreen {
    
    private Skin skin;
    private Texture titlescreenTexture;
    
    public MainMenuScreen() {
        skin = screenMgr.getAssets().get("Interface/Skin/medievania_skin.json");
        titlescreenTexture = screenMgr.getAssets().get("Interface/titlescreen.png");
        
        Table labelTable = new Table();
        labelTable.bottom().right().padRight(30f).padBottom(30f);
        labelTable.setFillParent(true);
        
        Label newGameLabel = new Label("New Game", skin);
        Label loadGameLabel = new Label("Load Game", skin);
        Label creditLabel = new Label("Credits", skin);
        Label exitLabel = new Label("Quit", skin);
        
        newGameLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("daaaaamn");
            }
        });
        
        labelTable.add(newGameLabel).padTop(5f).align(Align.right);
        labelTable.row();
        labelTable.add(loadGameLabel).padTop(5f).align(Align.right);
        labelTable.row();
        labelTable.add(creditLabel).padTop(5f).align(Align.right);
        labelTable.row();
        labelTable.add(exitLabel).padTop(5f).align(Align.right);
        
        addActor(labelTable);
    }
    

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            screenMgr.showScreen(GameScreen.GAME);
            dispose();
        }
    }
    
    @Override
    public void render(float delta) {
        handleInput(delta);
        
        CameraUtils.clearScreen();
        
        screenMgr.getBatch().begin();
        screenMgr.getBatch().draw(titlescreenTexture, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        screenMgr.getBatch().end();
        
        draw();
    }

}