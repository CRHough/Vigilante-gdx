package com.aesophor.medieval.screens;

import com.aesophor.medieval.Medieval;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {
    
    private Stage stage;
    
    private Table table;
    private TextButton startBtn;
    private TextButton exitBtn;
    
    public MainMenuScreen(Medieval game) {
        stage = new Stage(new FitViewport(Medieval.V_WIDTH / Medieval.PPM, Medieval.V_HEIGHT / Medieval.PPM));
        
        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());
        
        table.add(startBtn, exitBtn);
        stage.addActor(table);
    }
    

    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }
    
    private void update(float delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(float delta) {
        update(delta);
        
        // Clear the game screen with pure black.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
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
        // TODO Auto-generated method stub
        
    }

}