package com.aesophor.medievania.system;

import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.ui.StatusBars;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class PlayerStatusBarsSystem extends EntitySystem {

    private final Batch batch;
    private final StatusBars statusBars;

    public PlayerStatusBarsSystem(Batch batch, StatusBars statusBars) {
        this.batch = batch;
        this.statusBars = statusBars;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(statusBars.getCamera().combined);
        statusBars.update(delta);
        statusBars.draw();
    }

    public void registerPlayer(Player player) {
        statusBars.setPlayer(player);
    }

}
