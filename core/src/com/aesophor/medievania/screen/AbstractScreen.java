package com.aesophor.medievania.screen;

import com.aesophor.medievania.constants.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class AbstractScreen extends Stage implements Screen {
    
    public static final float VIEWPORT_WIDTH = Constants.V_WIDTH / Constants.PPM;
    public static final float VIEWPORT_HEIGHT = Constants.V_HEIGHT / Constants.PPM;
    
    protected ScreenManager screenMgr;
    
    protected AbstractScreen() {
        // Note that this default constructor does NOT scale the viewport with PPM!
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT , new OrthographicCamera()), ScreenManager.getInstance().getBatch());
        this.screenMgr = ScreenManager.getInstance();
    }
    
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }
 
    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
 
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

}
