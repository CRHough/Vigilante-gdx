package com.aesophor.medieval.screens;

import com.aesophor.medieval.Medieval;
import com.aesophor.medieval.misc.Font;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {
    
    private Game game;
    
    private Stage stage;
    private Viewport viewport;
    
    public GameOverScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(Medieval.V_WIDTH, Medieval.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Medieval) game).batch);
        
        Label.LabelStyle font = new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE);
        
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        
        Label gameOverLabel = new Label("GAME OVER", font);
        Label retryLabel = new Label("Click to retry", font);
        
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(retryLabel).expandX().padTop(10f);
        
        stage.addActor(table);
    }
    

    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    public void handleInput(float dt) {
        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen((Medieval) game));
            dispose();
        }
    }
    
    @Override
    public void render(float delta) {
        handleInput(delta);
        
        // Clear the game screen with pure black.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
