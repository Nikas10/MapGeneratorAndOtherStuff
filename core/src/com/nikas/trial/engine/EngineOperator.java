package com.nikas.trial.engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.nikas.trial.GameLauncher;
import com.nikas.trial.engine.configuration.ColoringConfiguration;
import com.nikas.trial.engine.configuration.enums.EnvironmentColorNaming;
import com.nikas.trial.model.component.PositionComponent;
import com.nikas.trial.model.component.mappers.MapperFactory;
import com.nikas.trial.model.entity.map.MapGenerationParameters;
import com.nikas.trial.screens.GameScreen;
import lombok.Getter;

import java.util.List;
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

    public int processMap(GameScreen gameScreen) {
        SpriteCache spriteCache = gameScreen.getSpriteCache();
        MapGenerationParameters mapGen = game.getMapGenerationParameters();
        Pixmap map = new Pixmap(mapGen.getMapSquareSize(),
                mapGen.getMapSquareSize(), Pixmap.Format.RGBA8888);
        ImmutableArray<Entity> entities = gameEngine.getEntitiesFor(Family.all(PositionComponent.class).get());
        for (Entity entity: entities) {
            PositionComponent positionComponent = MapperFactory.getPositions().get(entity);
            Color color = evaluateHeightColor(positionComponent.getZ(),mapGen);
            map.drawPixel(positionComponent.getX(), positionComponent.getY(), Color.rgba8888(
                    color
            ));
        }
        Texture texture = new Texture(map, Pixmap.Format.RGBA8888, true);
        spriteCache.beginCache();
        spriteCache.add(texture, 0, 0);
        return spriteCache.endCache();
    }

    public void drawCaches(GameScreen gameScreen) {
        List<Integer> caches = gameScreen.getAvailableCaches();
        SpriteCache spriteCache = gameScreen.getSpriteCache();
        spriteCache.setProjectionMatrix(gameScreen.getCameraManager().getCamera().combined);
        spriteCache.begin();
        for (Integer i : caches) {
            spriteCache.draw(i);
        }
        spriteCache.end();
    }

    public void clearEntities() {
        gameEngine.removeAllEntities();
    }

    private Color evaluateHeightColor(Float height, MapGenerationParameters mapGen) {
        //R G B Alpha
        Map<EnvironmentColorNaming, Color> environmentSkin = palette.getEnvironmentColors();
        Float defaultHeight = mapGen.getMapDefaultHeight();
        Float maxHeight = mapGen.getMapMaxLandHeight();
        Float minHeight = mapGen.getMapMinWaterHeight();
        Color color;
        float coeff;
        if (height <= defaultHeight) { // water
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
