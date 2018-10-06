package com.nikas.trial.model.component.mappers;

import com.badlogic.ashley.core.ComponentMapper;
import com.nikas.trial.model.component.IdentificatorComponent;
import com.nikas.trial.model.component.PositionComponent;
import lombok.Data;
import lombok.Getter;

@Data
public class MapperFactory {

    @Getter
    private static final ComponentMapper<PositionComponent> positions = ComponentMapper.getFor(PositionComponent.class);

    @Getter
    private static final ComponentMapper<IdentificatorComponent> identificators = ComponentMapper.getFor(IdentificatorComponent.class);

}
