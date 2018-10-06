package com.nikas.trial.engine.configuration;

import com.badlogic.gdx.graphics.Color;
import com.nikas.trial.engine.configuration.enums.EnvironmentColorNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Class for storing coloring configuration
 * for drawing environmental entities
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColoringConfiguration {

    private Map<EnvironmentColorNaming, Color> environmentColors;

}
