package com.aesophor.vigilante.screen;

import com.aesophor.vigilante.Vigilante;

public enum Screens {
    
    MAIN_MENU {
        public AbstractScreen newScreen(Vigilante gameStateManager, Object... params) {
            return new MainMenuScreen(gameStateManager);
        }
    },
    GAME {
        public AbstractScreen newScreen(Vigilante gameStateManager, Object... params) {
            return new MainGameScreen(gameStateManager);
        }
    },
    GAME_OVER {
        public AbstractScreen newScreen(Vigilante gameStateManager, Object... params) {
            return new GameOverScreen(gameStateManager);
        }
    };
 
    public abstract AbstractScreen newScreen(Vigilante gameStateManager, Object... params);
}