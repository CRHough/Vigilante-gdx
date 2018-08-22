package com.aesophor.medieval.scenes;

import com.aesophor.medieval.Medieval;
import com.aesophor.medieval.misc.Font;
import com.aesophor.medieval.sprites.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    
    public Stage stage;
    private Viewport viewport; 
    
    private Player player;
    
    private int worldTimer;
    private float timeCount;
    private int score;
    
    private Label countdownLabel;
    private Label healthLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label playerNameLabel;
    
    public Hud(Player player, SpriteBatch sb) {
        this.player = player;
        
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        
        viewport = new FitViewport(Medieval.V_WIDTH, Medieval.V_HEIGHT);
        stage = new Stage(viewport, sb);
        
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        
        
        playerNameLabel = new Label("PLAYER", new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        healthLabel = new Label(String.format("HEALTH: %03d", player.getHealth()), new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        
        worldLabel = new Label(" ", new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        levelLabel = new Label(" ", new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        
        timeLabel = new Label("STARTING POINT", new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        //countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(Font.getDefaultFont(), Color.WHITE));
        
        
        table.add(playerNameLabel).expandX().pad(10);
        table.add(worldLabel).expandX().pad(10);
        table.add(timeLabel).expandX().pad(10);
        table.row();
        table.add(healthLabel).expandX();
        table.add(levelLabel).expandX();
        //table.add(countdownLabel).expandX();
        
        
        stage.addActor(table);
    }
    
    
    public void update(float dt) {
        healthLabel.setText("HEALTH: " + Integer.toString(player.getHealth()));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    
}