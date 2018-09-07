package com.aesophor.medievania.system;

import com.aesophor.medievania.ui.DamageIndicator;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DamageIndicatorSystem extends EntitySystem {

    private final Stage mainGameStage;
    private final DamageIndicator damageIndicator;

    public DamageIndicatorSystem(Stage mainGameStage, DamageIndicator damageIndicator) {
        this.mainGameStage = mainGameStage;
        this.damageIndicator = damageIndicator;
    }


    @Override
    public void update(float delta) {
        mainGameStage.getBatch().setProjectionMatrix(mainGameStage.getCamera().combined);
        damageIndicator.update(delta);
        mainGameStage.act(delta);
        mainGameStage.draw();
    }

}