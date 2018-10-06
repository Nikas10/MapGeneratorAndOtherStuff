package com.nikas.trial.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nikas.trial.engine.configuration.CameraOffset;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CameraManager {
    private OrthographicCamera camera;

    public void applyCameraChanges(CameraOffset cameraOffset) {
        if (cameraOffset.getHorizontalDelta() != 0.0f) {
            camera.translate(cameraOffset.getHorizontalDelta(),0,0);
        }
        if (cameraOffset.getVerticalDelta() != 0.0f) {
            camera.translate(0,cameraOffset.getVerticalDelta(),0);
        }
        if (cameraOffset.getZoomDelta() != 0.0f) {
            if (!((camera.zoom <= 1) && (cameraOffset.getZoomDelta() < 0))) {
                camera.zoom += cameraOffset.getZoomDelta();
            }
        }
    }
}
