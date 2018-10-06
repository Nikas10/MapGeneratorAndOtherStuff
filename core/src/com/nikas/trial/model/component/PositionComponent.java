package com.nikas.trial.model.component;

import com.badlogic.ashley.core.Component;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PositionComponent implements Component {
    private Integer x = 0;
    private Integer y = 0;
    private Float z = 0.0f; //height
}
