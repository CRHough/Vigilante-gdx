package com.aesophor.medievania.system;

import com.aesophor.medievania.ui.pausemenu.PauseMenu;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class PauseMenuSystem extends EntitySystem {

    private final Batch batch;
    private final PauseMenu pauseMenu;

    public PauseMenuSystem(Batch batch, PauseMenu pauseMenu) {
        this.batch = batch;
        this.pauseMenu = pauseMenu;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(pauseMenu.getCamera().combined);
        pauseMenu.update(delta);
        pauseMenu.draw();
    }

}