package com.nikas.trial.model.entity.map;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for storing game grid information
 */
@Data
@AllArgsConstructor
public class MapGridDto implements Serializable {
    private Integer x = 0;
    private Integer y = 0;
    private Float height = 0.0f;
}
