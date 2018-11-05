package com.aesophor.vigilante.screen;

import com.aesophor.vigilante.GameStateManager;
import com.aesophor.vigilante.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class AbstractScreen extends Stage implements Screen {
    
    protected GameStateManager gsm;
    protected boolean paused;
    
    protected AbstractScreen(GameStateManager gsm) {
        // Note that this default constructor does NOT scale the viewport with PPM!
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT , new OrthographicCamera()), gsm.getBatch());
        this.gsm = gsm;
        Gdx.input.setInputProcessor(this);
    }

 
    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override public void show() {}
    @Override public void hide() {}

}