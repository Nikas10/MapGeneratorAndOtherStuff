package com.nikas.trial.engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.nikas.trial.model.component.PositionComponent;
import com.nikas.trial.model.component.mappers.MapperFactory;
import com.nikas.trial.model.entity.configuration.CameraOffset;
import com.nikas.trial.model.entity.map.MapGenerationParameters;
import lombok.Getter;


/**
 * Class-aggregator for Ashley framework Engine class
 * provides high-level functional for use in internal logic
 * for reducing code complexivity.
 */
public class EngineOperator {

    @Getter
    private Engine gameEngine;

    public EngineOperator() {
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

    public void resizeEntities(int width, int height) {
    }

    //todo parametrized colours (colour packs)
    private Color evaluateHeightColor(Float height, MapGenerationParameters mapGen) {
        //R G B Alpha
        Float defaultHeight = mapGen.getMapDefaultHeight();
        Float maxHeight = mapGen.getMapMaxLandHeight();
        Float minHeight = mapGen.getMapMinWaterHeight();
        Color color; //blue
        float coeff;
        if (height <= defaultHeight) { //<0 water
            color = new Color(0.3f, 0.3f, 0.6f, 1);
            coeff = (height - minHeight * 0.75f) / (defaultHeight - minHeight * 0.75f);
            color = color.lerp(0.8f,0.8f,0.9f, 1, coeff);
            return color;
        }
        else if (height < maxHeight / 2){ // land
            color = new Color(0.4f, 0.6f, 0.4f, 1);
            coeff = (height - defaultHeight) / ((maxHeight / 2) - defaultHeight);
            color = color.lerp(0.92f,0.76f,0.0f, 1, coeff);
            return color;
        }
        else { // high land
            color = new Color(0.92f,0.76f,0.0f, 1);
            coeff = (height - (maxHeight / 2)) / (maxHeight - (maxHeight / 2));
            color = color.lerp(0.63f,0.11f,0.0f, 1, coeff);
            return color;
        }
    }
}
