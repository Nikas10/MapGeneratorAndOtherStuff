package com.nikas.trial.model.entity.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CameraOffset {
    private Integer xOffset;
    private Integer yOffset;
    private Integer renderSquareLenght;
}
