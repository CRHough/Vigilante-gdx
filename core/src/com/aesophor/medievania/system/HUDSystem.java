package com.aesophor.medievania.system;

import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.ui.hud.HUD;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class HUDSystem extends EntitySystem {

    private final Batch batch;
    private final HUD HUD;

    public HUDSystem(Batch batch, HUD HUD) {
        this.batch = batch;
        this.HUD = HUD;
    }


    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(HUD.getCamera().combined);
        HUD.update(delta);
        HUD.draw();
    }

    public void registerPlayer(Player player) {
        HUD.registerPlayer(player);
    }

}
