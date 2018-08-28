package com.aesophor.medievania.screen;

import com.aesophor.medievania.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameOver extends AbstractScreen {
    
    private static final String SKIN_FILE = "Interface/Skin/medievania_skin.json";
    
    private Skin skin;
    
    public GameOver(GameStateManager gameStateManager) {
        super(gameStateManager);
        
        skin = gameStateManager.getAssets().get(SKIN_FILE);
        
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        
        Label gameOverLabel = new Label("GAME OVER", skin);
        Label retryLabel = new Label("Click to retry", skin);
        
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(retryLabel).expandX().padTop(10f);
        
        addActor(table);
        
    }

    public void handleInput(float dt) {
        if (Gdx.input.justTouched()) {
            gameStateManager.showScreen(Screens.GAME);
            dispose();
        }
    }
    
    @Override
    public void render(float delta) {
        handleInput(delta);
        gameStateManager.clearScreen();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
    }

}
