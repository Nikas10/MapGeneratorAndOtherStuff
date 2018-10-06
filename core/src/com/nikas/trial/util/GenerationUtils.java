package com.nikas.trial.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.nikas.trial.model.component.IdentificatorComponent;
import com.nikas.trial.model.component.PositionComponent;
import com.nikas.trial.model.component.mappers.MapperFactory;
import com.nikas.trial.model.entity.DrawGroup;
import com.nikas.trial.model.entity.map.MapGenerationParameters;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for staff generation (maps, objects, etc) random based and not
 */
public class GenerationUtils {

    private static ComponentMapper<PositionComponent> positions = MapperFactory.getPositions();
    private static final Random randomGen = new Random();
    private static final float gaussianDeviation = (float) 1 / 2; //96% lies in [-1,1] range
    private static Integer[][] neighborDeltas = new Integer[][]{ //8 - 2 array of neighbor location, delta x and y
            {0,1},
            {1,0},
            {-1,0},
            {0,-1},
            {1,1},
            {1,-1},
            {-1,1},
            {-1,-1}
    };

    private Float getRandomGaussianDelta(Float bottom, Float top) {
        Float result = top + 1;
        while (result > top) {
            result = bottom + Math.abs( (float)randomGen.nextGaussian() * gaussianDeviation * (top - bottom));
        }
        return result;
    }

    public List<Entity> generateMap(MapGenerationParameters mapGenerationParameters) {
        Integer mapSize = mapGenerationParameters.getMapSquareSize();
        Float startHeight = mapGenerationParameters.getMapMinWaterHeight();
        Entity[][] map = new Entity[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++ ) {
                Entity brick = new Entity();
                brick.add(new IdentificatorComponent("environmentGridElement", DrawGroup.ENVIRONMENT));
                brick.add(new PositionComponent(i, j, startHeight));
                map[i][j] = brick;
            }
        }
        map = generateHeightDeltas(map, mapGenerationParameters);
        List<Entity> result = postProcessMap(map, mapSize);
        return result;
    }

    private Entity[][] generateHeightDeltas(Entity[][] map, MapGenerationParameters mapGenerationParameters) {
        Integer mapSize = mapGenerationParameters.getMapSquareSize();
        Float mapHeight = mapGenerationParameters.getMapMaxLandHeight();
        Queue<CoordEntry> renderQueue = new ArrayDeque<>();
        Map<String, CoordEntry> used = new HashMap<>();
        Queue<CoordEntry> highQueue = new ArrayDeque<>();
        for (int i = 0; i < mapGenerationParameters.getMapHighPointsCount(); i++) {
            Integer x = ThreadLocalRandom.current().nextInt(0, mapSize);
            Integer y = ThreadLocalRandom.current().nextInt(0, mapSize);
            Float height = getRandomGaussianDelta(0.0f, mapHeight);
            if (randomGen.nextGaussian()>=0)
                ((ArrayDeque<CoordEntry>) highQueue).addFirst(new CoordEntry(x, y, height));
            else highQueue.add(new CoordEntry(x, y, height));
        }
        highQueue = generateRenderQueue(highQueue, used, mapGenerationParameters);
        renderQueue.addAll(highQueue);
        map = adjustHeights(map, renderQueue);
        return map;
    }

    private Queue<CoordEntry> generateRenderQueue(Queue<CoordEntry> queue, Map<String, CoordEntry> used, MapGenerationParameters mapGen) {
        if (queue.isEmpty()) return queue;
        Integer mapSize = mapGen.getMapSquareSize();
        Float minHeight = mapGen.getMapMinWaterHeight();
        Float stepDelta = mapGen.getMapHeightGradationLimit();
        Queue<CoordEntry> neighbors = new ArrayDeque<>();
        for (CoordEntry entry: queue) {
            Integer x = entry.getX();
            Integer y = entry.getY();
            Float height = entry.getZ();
            for (int i = 0; i < 8; i++) {
                if ((x + neighborDeltas[i][0] >= 0) && (x + neighborDeltas[i][0] < mapSize)) {
                    if ((y + neighborDeltas[i][1] >= 0) && (y + neighborDeltas[i][1] < mapSize)) {
                        Integer newX = x + neighborDeltas[i][0];
                        Integer newY = y + neighborDeltas[i][1];
                        Float currentDelta = getRandomGaussianDelta(0.0f, stepDelta);
                        if (i > 3) { //diagonal entries
                            currentDelta = (float)Math.sqrt(2) * currentDelta;
                        }
                        currentDelta = -currentDelta;
                        if (height + currentDelta <= minHeight) continue;
                        CoordEntry newEntry = new CoordEntry(newX, newY, height + currentDelta);
                        if (!queue.contains(newEntry) && (!neighbors.contains(newEntry))) {
                            boolean chaosFactor = randomGen.nextGaussian() >= 0;
                            boolean draw = true;
                            if (used.containsKey(newEntry.getId())) {
                                CoordEntry usedEntry = used.get(newEntry.getId());
                                draw = checkHeights(usedEntry.getZ(), newEntry.getZ());
                            }
                            if (draw) {
                                if (chaosFactor) {
                                    ((ArrayDeque<CoordEntry>) neighbors).addFirst(newEntry);
                                } else {
                                    neighbors.add(newEntry);
                                }
                            }
                        }
                    }
                }
            }
        }
        queue.forEach(element -> used.put(element.getId(), element));
        queue.addAll(neighbors);
        queue.addAll(generateRenderQueue(neighbors, used, mapGen));
        return queue;
    }

    private boolean checkHeights(Float z1, Float z2) {
        return z2 > z1;
    }

    private Entity[][] adjustHeights(Entity[][] map, Queue<CoordEntry> queue) {
        while (!queue.isEmpty()) {
            CoordEntry entry = queue.poll();
            positions.get(map[entry.getX()][entry.getY()]).setZ(entry.getZ());
        }
        return map;
    }

    private List<Entity> postProcessMap(Entity[][] map, Integer mapSize) {
        List<Entity> result = new ArrayList<>();
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                result.add(map[i][j]);
            }
        }
        return result;
    }
}
