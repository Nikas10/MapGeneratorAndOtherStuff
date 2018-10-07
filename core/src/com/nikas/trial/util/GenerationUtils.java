package com.nikas.trial.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.nikas.trial.model.component.IdentificatorComponent;
import com.nikas.trial.model.component.PositionComponent;
import com.nikas.trial.model.component.mappers.MapperFactory;
import com.nikas.trial.model.entity.DrawGroup;
import com.nikas.trial.model.entity.map.MapGenerationParameters;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
        Map<String, CoordEntry> used = new ConcurrentHashMap<>();
        for (int i = 0; i < mapGenerationParameters.getMapHighPointsCount(); i++) {
            Integer x = ThreadLocalRandom.current().nextInt(0, mapSize);
            Integer y = ThreadLocalRandom.current().nextInt(0, mapSize);
            Float height = getRandomGaussianDelta(0.0f, mapHeight);
            if (randomGen.nextGaussian()>=0) {
                ((ArrayDeque<CoordEntry>) renderQueue).addFirst(new CoordEntry(x, y, height));
            }
            else {
                renderQueue.add(new CoordEntry(x, y, height));
            }
        }
        //renderQueue = generateRenderQueue(renderQueue, used, mapGenerationParameters); //deprecated
        renderQueue = renderQueue.parallelStream().flatMap(coordEntry -> {
            Queue<CoordEntry> simpleQueue = new ArrayDeque<>();
            simpleQueue.add(coordEntry);
            return generateParallelRenderQueue(simpleQueue, used, mapGenerationParameters).stream();
        }).collect(Collectors.toCollection(ArrayDeque::new));
        map = adjustHeights(map, renderQueue);
        return map;
    }


    /**
     * Generate render queue method based on unparallel BFS
     */
    @Deprecated
    private Queue<CoordEntry> generateRenderQueue(Queue<CoordEntry> queue, Map<String, CoordEntry> used, MapGenerationParameters mapGen) {
        if (queue.isEmpty()) return queue;
        queue.forEach(element -> used.put(element.getId(), element));
        Integer mapSize = mapGen.getMapSquareSize();
        Float minHeight = mapGen.getMapMinWaterHeight();
        Float stepDelta = mapGen.getMapHeightGradationLimit();
        ArrayDeque<CoordEntry> neighbors = new ArrayDeque<>();
        for (CoordEntry entry: queue) {
            Integer x = entry.getX();
            Integer y = entry.getY();
            Float height = entry.getZ();
            //processing neighbors
            for (int i = 0; i < 8; i++) {
                if (checkBorders(x, y, mapSize, i)) {
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
                            draw = usedEntry.getZ() < newEntry.getZ();
                        }
                        if (draw) {
                            if (chaosFactor) {
                                neighbors.addFirst(newEntry);
                            } else {
                                neighbors.add(newEntry);
                            }
                        }
                    }
                }
            }
        }
        queue.addAll(generateRenderQueue(neighbors, used, mapGen));
        return queue;
    }

    /**
     * Generate render queue method based on parallel BFS on Stream API (fork/join tool)
     * @param queue nodes to traverse on iteration
     * @param used used nodes (volatile map for all threads, changes behavior of algorithm)
     * @param mapGen generation parameters
     * @return Render queue
     */
    private Queue<CoordEntry> generateParallelRenderQueue(Queue<CoordEntry> queue, Map<String, CoordEntry> used, MapGenerationParameters mapGen) {
        if (queue.isEmpty()) return queue;
        queue.forEach(element -> used.put(element.getId(), element));
        Integer mapSize = mapGen.getMapSquareSize();
        Float minHeight = mapGen.getMapMinWaterHeight();
        Float stepDelta = mapGen.getMapHeightGradationLimit();
        ArrayDeque<CoordEntry> neighbors = new ArrayDeque<>();
        for (CoordEntry entry: queue) {
            Integer x = entry.getX();
            Integer y = entry.getY();
            Float height = entry.getZ();
            for (int i = 0; i < 8; i++) {
                if (checkBorders(x, y, mapSize, i)) {
                    Integer newX = x + neighborDeltas[i][0];
                    Integer newY = y + neighborDeltas[i][1];
                    Float currentDelta = getRandomGaussianDelta(0.0f, stepDelta);
                    if (i > 3) { //diagonal entries
                        currentDelta = (float)Math.sqrt(2) * currentDelta;
                    }
                    currentDelta = -currentDelta;
                    if (height + currentDelta <= minHeight) continue;
                    CoordEntry newEntry = new CoordEntry(newX, newY, height + currentDelta);
                    boolean chaosFactor = randomGen.nextGaussian() >= 0;
                    boolean draw = true;
                    if (used.containsKey(newEntry.getId())) {
                        CoordEntry usedEntry = used.get(newEntry.getId());
                        draw = usedEntry.getZ() < newEntry.getZ();
                    }
                    if (draw) {
                        if (chaosFactor) {
                            neighbors.addFirst(newEntry);
                        } else {
                            neighbors.add(newEntry);
                        }
                    }
                }
            }
        }
        if (neighbors.isEmpty()) return queue;
        queue.addAll(neighbors.parallelStream().flatMap(coordEntry -> {
            Queue<CoordEntry> simpleQueue = new ArrayDeque<>();
            simpleQueue.add(coordEntry);
            return generateParallelRenderQueue(simpleQueue, used, mapGen).stream();
        }).collect(Collectors.toCollection(ArrayDeque::new)));
        return queue;
    }

    private boolean checkBorders(Integer x, Integer y, Integer mapSize, Integer neighbor) {
        return ((x + neighborDeltas[neighbor][0] >= 0) && (x + neighborDeltas[neighbor][0] < mapSize)) &&
             ((y + neighborDeltas[neighbor][1] >= 0) && (y + neighborDeltas[neighbor][1] < mapSize));
    }

    private Entity[][] adjustHeights(Entity[][] map, Queue<CoordEntry> queue) {
        while (!queue.isEmpty()) {
            CoordEntry entry = queue.poll();
            if (positions.get(map[entry.getX()][entry.getY()]).getZ() < entry.getZ()) {
                positions.get(map[entry.getX()][entry.getY()]).setZ(entry.getZ());
            }
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
