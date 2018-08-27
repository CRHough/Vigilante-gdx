package com.aesophor.medievania.screen;

import com.aesophor.medievania.util.Font;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameOverScreen extends AbstractScreen {
    
    public GameOverScreen() {
        Label.LabelStyle font = new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE);
        
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        
        Label gameOverLabel = new Label("GAME OVER", font);
        Label retryLabel = new Label("Click to retry", font);
        
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(retryLabel).expandX().padTop(10f);
        
        addActor(table);
    }

    public void handleInput(float dt) {
        if (Gdx.input.justTouched()) {
            screenMgr.showScreen(GameScreen.GAME);
            dispose();
        }
    }
    
    @Override
    public void render(float delta) {
        handleInput(delta);
        screenMgr.clearScreen();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
    }

}
