package com.nikas.trial.engine.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nikas.trial.engine.configuration.CameraOffset;
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
        CameraOffset cameraOffset = gameScreen.getCameraOffset();
        switch (keycode) {
            // z x rendering scale (deprecated)
            case Input.Keys.Z : {
                cameraOffset.setRenderSquareLength(
                        cameraOffset.getRenderSquareLength() + 1);
                break;
            }
            case Input.Keys.X : {
                cameraOffset.setRenderSquareLength(
                        cameraOffset.getRenderSquareLength() - 1);
                break;
            }
            //q e zooming
            case Input.Keys.Q : {
                cameraOffset.setZoomDelta(-1f);
                break;
            }
            case Input.Keys.E : {
                cameraOffset.setZoomDelta(1f);
                break;
            }
            //wasd
            case Input.Keys.W : {
                cameraOffset.setVerticalDelta((float)cameraOffset.getRenderSquareLength());
                break;
            }
            case Input.Keys.A : {
                cameraOffset.setHorizontalDelta(-(float)cameraOffset.getRenderSquareLength());
                break;
            }
            case Input.Keys.S : {
                cameraOffset.setVerticalDelta(-(float)cameraOffset.getRenderSquareLength());
                break;
            }
            case Input.Keys.D : {
                cameraOffset.setHorizontalDelta((float)cameraOffset.getRenderSquareLength());
                break;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        //q e zooming
        CameraOffset cameraOffset = gameScreen.getCameraOffset();
        switch (keycode) {
            case Input.Keys.Q : {
                cameraOffset.setZoomDelta(0.0f);
                break;
            }
            case Input.Keys.E : {
                cameraOffset.setZoomDelta(0.0f);
                break;
            }
            //wasd
            case Input.Keys.W : {
                cameraOffset.setVerticalDelta(0.0f);
                break;
            }
            case Input.Keys.A : {
                cameraOffset.setHorizontalDelta(0.0f);
                break;
            }
            case Input.Keys.S : {
                cameraOffset.setVerticalDelta(0.0f);
                break;
            }
            case Input.Keys.D : {
                cameraOffset.setHorizontalDelta(0.0f);
                break;
            }
        }
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
