package com.nikas.trial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nikas.trial.GameLauncher;
import com.nikas.trial.engine.CameraManager;
import com.nikas.trial.engine.configuration.CameraOffset;
import com.nikas.trial.engine.input.KeyboardInputProcessor;
import com.nikas.trial.util.GenerationUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameScreen implements Screen {

    private GameLauncher game;
    private Stage stage;
    private SpriteCache spriteCache;
    private List<Integer> availableCaches;

    private TextButton playButton;
    private GenerationUtils generationUtils = new GenerationUtils();

    private CameraManager cameraManager;
    private CameraOffset cameraOffset = new CameraOffset(0.0f,0.0f,0.0f, 0.0f);

    public GameScreen(GameLauncher game) {
        this.game = game;
        this.spriteCache = new SpriteCache(1000, true);
        this.availableCaches = new ArrayList<>();
    }

    private void defineUiComponents() {
        playButton = new TextButton("Menu", game.getUiSkin());
        Integer windowWidth = Gdx.graphics.getWidth();
        Integer windowHeight = Gdx.graphics.getHeight();
        playButton.setWidth(windowWidth / 8);
        playButton.setHeight(windowHeight / 16);
        playButton.setPosition(windowWidth, windowHeight);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(GameLauncher.getFactory().getTitleInstance());
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    private void defineEntities() {
        generationUtils.generateMap(game.getMapGenerationParameters()).forEach(entity ->
                game.getEngineOperator().getGameEngine().addEntity(entity));
    }

    private void drawUi() {
        stage.addActor(playButton);
    }

    @Override
    public void show() {
        this.stage = new Stage(new StretchViewport(game.getScreenParameters().getScreenWidth(),
                game.getScreenParameters().getScreenHeight()));
        this.cameraManager = new CameraManager(new OrthographicCamera(stage.getWidth() / 4,
                stage.getWidth() / 4 * (stage.getHeight() / stage.getWidth())));
        OrthographicCamera camera = cameraManager.getCamera();
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new KeyboardInputProcessor(this));

        camera.position.set(stage.getWidth() / 2, stage.getHeight() / 2, 0);
        camera.update();

        game.getEngineOperator().clearEntities();
        defineEntities();
        availableCaches.add(game.getEngineOperator().processMap(this));
        defineUiComponents();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cameraManager.applyCameraChanges(cameraOffset);
        cameraManager.getCamera().update();
        game.getEngineOperator().drawCaches(this);
        drawUi();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        playButton.setWidth(width / 8);
        playButton.setHeight(height / 16);
        playButton.setPosition(width - playButton.getWidth(),height - playButton.getHeight());
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
