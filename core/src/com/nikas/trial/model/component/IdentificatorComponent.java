package com.nikas.trial.model.component;

import com.badlogic.ashley.core.Component;
import com.nikas.trial.model.entity.DrawGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdentificatorComponent implements Component {
    private String name;
    private DrawGroup drawGroup;
}
