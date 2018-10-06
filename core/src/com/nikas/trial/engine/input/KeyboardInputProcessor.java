package com.nikas.trial.engine.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.nikas.trial.screens.GameScreen;

/**
 * Keyboard listener for game engine
 * methods for touch and mouse are not implemented!
 */
public class KeyboardInputProcessor implements InputProcessor {

    private GameScreen gameScreen;

    public KeyboardInputProcessor(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.Z : {
                gameScreen.getCameraOffset().setRenderSquareLength(
                        gameScreen.getCameraOffset().getRenderSquareLength() + 1);
                break;
            }
            case Input.Keys.X : {
                gameScreen.getCameraOffset().setRenderSquareLength(
                        gameScreen.getCameraOffset().getRenderSquareLength() - 1);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
