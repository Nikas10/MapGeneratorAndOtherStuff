package com.nikas.trial.engine.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CameraOffset {
    private Integer xOffset;
    private Integer yOffset;
    private Integer renderSquareLength;
    private Float zoomDelta;
    private Float verticalDelta;
    private Float horizontalDelta;
}
