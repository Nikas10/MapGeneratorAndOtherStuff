package com.nikas.trial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nikas.trial.GameLauncher;


public class TitleScreen implements Screen {

    private GameLauncher game;
    private Stage stage;
    private TextButton playButton;

    public TitleScreen(GameLauncher game) {
        this.game = game;
    }

    private void defineEntities() {
        playButton = new TextButton("Play!", game.getUiSkin());
        int windowWidth = Gdx.graphics.getWidth();
        int windowHeight = Gdx.graphics.getHeight();
        playButton.setWidth(windowWidth / 2);
        playButton.setPosition(windowWidth / 2 - playButton.getWidth() / 2,windowHeight / 2 - playButton.getHeight() / 2);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(GameLauncher.getFactory().getGameInstance());
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);
    }

    @Override
    public void show() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        defineEntities();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        playButton.setWidth(width / 2);
        playButton.setPosition(width / 2 - playButton.getWidth() / 2,height / 2 - playButton.getHeight() / 2);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
