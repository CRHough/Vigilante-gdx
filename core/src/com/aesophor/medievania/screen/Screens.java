package com.aesophor.medievania.screen;

import com.aesophor.medievania.Medievania;

public enum Screens {
    
    MAIN_MENU {
        public AbstractScreen newScreen(Medievania gameStateManager, Object... params) {
            return new MainMenu(gameStateManager);
        }
    },
    GAME {
        public AbstractScreen newScreen(Medievania gameStateManager, Object... params) {
            return new MainGame(gameStateManager);
        }
    },
    GAME_OVER {
        public AbstractScreen newScreen(Medievania gameStateManager, Object... params) {
            return new GameOver(gameStateManager);
        }
    };
 
    public abstract AbstractScreen newScreen(Medievania gameStateManager, Object... params);
}