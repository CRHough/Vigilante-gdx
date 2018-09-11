package com.aesophor.medievania.screen;

import com.aesophor.medievania.Medievania;

public enum Screens {
    
    MAIN_MENU {
        public AbstractScreen newScreen(Medievania gameStateManager, Object... params) {
            return new MainMenuScreen(gameStateManager);
        }
    },
    GAME {
        public AbstractScreen newScreen(Medievania gameStateManager, Object... params) {
            return new MainGameScreen(gameStateManager);
        }
    },
    GAME_OVER {
        public AbstractScreen newScreen(Medievania gameStateManager, Object... params) {
            return new GameOverScreen(gameStateManager);
        }
    };
 
    public abstract AbstractScreen newScreen(Medievania gameStateManager, Object... params);
}