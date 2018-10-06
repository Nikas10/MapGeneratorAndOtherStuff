package com.nikas.trial.engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.nikas.trial.GameLauncher;
import com.nikas.trial.engine.configuration.ColoringConfiguration;
import com.nikas.trial.engine.configuration.enums.EnvironmentColorNaming;
import com.nikas.trial.model.component.PositionComponent;
import com.nikas.trial.model.component.mappers.MapperFactory;
import com.nikas.trial.engine.configuration.CameraOffset;
import com.nikas.trial.model.entity.map.MapGenerationParameters;
import lombok.Getter;

import java.util.Map;


/**
 * Class-aggregator for Ashley framework Engine class
 * provides high-level functional for use in internal logic
 * for reducing code complexivity.
 */
public class EngineOperator {

    @Getter
    private GameLauncher game;
    @Getter
    private Engine gameEngine;
    @Getter
    private ColoringConfiguration palette;

    public EngineOperator(GameLauncher game)
    {
        this.game = game;
        palette = this.game.getColoringConfiguration();
        gameEngine = new Engine();
    }

    public void processMap(CameraOffset cameraOffset, ShapeRenderer shapeRenderer, MapGenerationParameters mapGen) {
        ImmutableArray<Entity> entities = gameEngine.getEntitiesFor(Family.all(PositionComponent.class).get());
        Integer primitiveSize = cameraOffset.getRenderSquareLenght();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity entity: entities) {
            PositionComponent positionComponent = MapperFactory.getPositions().get(entity);
            Color color = evaluateHeightColor(positionComponent.getZ(),mapGen);
            if (((positionComponent.getX() - cameraOffset.getXOffset()) * primitiveSize > Gdx.graphics.getWidth())
                    || ((positionComponent.getY() - cameraOffset.getYOffset()) * primitiveSize > Gdx.graphics.getHeight())) continue;
            if (positionComponent.getX() >= cameraOffset.getXOffset()
                    && positionComponent.getY() >= cameraOffset.getYOffset()) {
                shapeRenderer.rect((positionComponent.getX() - cameraOffset.getXOffset()) * primitiveSize,
                        (positionComponent.getY() - cameraOffset.getYOffset()) * primitiveSize,
                        primitiveSize,
                        primitiveSize,
                        color, color, color, color);
            }
        }
        shapeRenderer.end();
    }

    //todo parametrized colours (colour packs)
    private Color evaluateHeightColor(Float height, MapGenerationParameters mapGen) {
        //R G B Alpha
        Map<EnvironmentColorNaming, Color> environmentSkin = palette.getEnvironmentColors();
        Float defaultHeight = mapGen.getMapDefaultHeight();
        Float maxHeight = mapGen.getMapMaxLandHeight();
        Float minHeight = mapGen.getMapMinWaterHeight();
        Color color; //blue
        float coeff;
        if (height <= defaultHeight) { //<0 water
            color = new Color(environmentSkin.get(EnvironmentColorNaming.WATER_LOW));
            coeff = (height - minHeight * 0.75f) / (defaultHeight - minHeight * 0.75f);
            return color.lerp(environmentSkin.get(EnvironmentColorNaming.WATER_HIGH), coeff);
        }
        else if (height < maxHeight / 2){ // land
            color = new Color(environmentSkin.get(EnvironmentColorNaming.LAND_LOW));
            coeff = (height - defaultHeight) / ((maxHeight / 2) - defaultHeight);
            return color.lerp(environmentSkin.get(EnvironmentColorNaming.LAND_MEDIUM), coeff);
        }
        else { // high land
            color = new Color(environmentSkin.get(EnvironmentColorNaming.LAND_MEDIUM));
            coeff = (height - (maxHeight / 2)) / (maxHeight - (maxHeight / 2));
            return color.lerp(environmentSkin.get(EnvironmentColorNaming.LAND_HIGH), coeff);
        }
    }
}
