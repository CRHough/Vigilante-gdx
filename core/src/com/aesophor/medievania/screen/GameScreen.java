package com.aesophor.medievania.screen;

public enum GameScreen {
    
    MAIN_MENU {
        public AbstractScreen newScreen(Object... params) {
            return new MainMenuScreen();
        }
    },
    GAME {
        public AbstractScreen newScreen(Object... params) {
            return new PlayScreen();
        }
    },
    GAME_OVER {
        public AbstractScreen newScreen(Object... params) {
            return new GameOverScreen();
        }
    };
 
    public abstract AbstractScreen newScreen(Object... params);
}