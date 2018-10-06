package com.nikas.trial.model.entity.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that stores generation parameters to pass them to game screen during new game processing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MapGenerationParameters {
    private Integer mapSquareSize = 100;
    private Integer mapHighPointsCount = 1;
    private Float mapDefaultHeight = 0.0f;
    private Float mapMaxLandHeight = 200.0f;
    private Float mapMinWaterHeight = 200.0f;
    private Float mapHeightGradationLimit = 20.0f;
}
