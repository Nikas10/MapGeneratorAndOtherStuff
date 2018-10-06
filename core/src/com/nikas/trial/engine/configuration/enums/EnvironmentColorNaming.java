package com.nikas.trial.engine.configuration.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EnvironmentColorNaming {
    @JsonProperty("water-low")
    WATER_LOW,
    @JsonProperty("water-high")
    WATER_HIGH,
    @JsonProperty("land-low")
    LAND_LOW,
    @JsonProperty("land-medium")
    LAND_MEDIUM,
    @JsonProperty("land-high")
    LAND_HIGH,
}