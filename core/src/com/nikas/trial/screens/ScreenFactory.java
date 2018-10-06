package com.nikas.trial.screens;

import com.nikas.trial.GameLauncher;

public class ScreenFactory {
    private TitleScreen titleScreen;
    private GameScreen gameScreen;

    public ScreenFactory(GameLauncher game) {
        this.gameScreen = new GameScreen(game);
        this.titleScreen = new TitleScreen(game);
    }

    public TitleScreen getTitleInstance() {
        return titleScreen;
    }

    public GameScreen getGameInstance() {
        return gameScreen;
    }
}
