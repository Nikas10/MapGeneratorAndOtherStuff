package com.nikas.trial.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"z"})
public class CoordEntry {
    private Integer x;
    private Integer y;
    private Float z;

    public String getId() {
        return x.toString() + " " + y.toString();
    }
}
