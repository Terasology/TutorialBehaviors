// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.minion.move;

import com.google.common.collect.Lists;
import org.joml.RoundingMode;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.logic.behavior.BehaviorAction;
import org.terasology.logic.behavior.core.Actor;
import org.terasology.logic.behavior.core.BaseAction;
import org.terasology.logic.behavior.core.BehaviorState;
import org.terasology.logic.location.LocationComponent;
import org.terasology.minion.component.FleeComponent;
import org.terasology.navgraph.WalkableBlock;
import org.terasology.pathfinding.componentSystem.PathfinderSystem;
import org.terasology.registry.In;

import java.util.List;
import java.util.Random;

import static java.lang.Integer.min;

@BehaviorAction(name = "set_target_nearby_block_away_from_player")
public class SetTargetToNearbyBlockAwayFromPlayerAction extends BaseAction {

    @In
    private PathfinderSystem pathfinderSystem;


    private static final Logger logger = LoggerFactory.getLogger(SetTargetToNearbyBlockAwayFromPlayerAction.class);


    private static final int RANDOM_BLOCK_ITERATIONS = 10;
    private Random random = new Random();


    @Override
    public BehaviorState modify(Actor actor, BehaviorState state) {
        MinionMoveComponent moveComponent = actor.getComponent(MinionMoveComponent.class);
        if (moveComponent.currentBlock != null) {
            WalkableBlock target = findRandomNearbyBlockAwayFromPlayer(moveComponent.currentBlock, actor);
            moveComponent.target = new Vector3f(target.getBlockPosition());
            actor.save(moveComponent);
        } else {
            return BehaviorState.FAILURE;
        }
        return BehaviorState.SUCCESS;
    }

    private WalkableBlock findRandomNearbyBlockAwayFromPlayer(WalkableBlock startBlock, Actor actor) {
        WalkableBlock currentBlock = startBlock;
        Vector3i playerPosition =
                new Vector3i(actor.getComponent(FleeComponent.class).instigator.getComponent(LocationComponent.class).getWorldPosition(new Vector3f()), RoundingMode.FLOOR);
        for (int i = 0; i < RANDOM_BLOCK_ITERATIONS; i++) {
            WalkableBlock[] neighbors = currentBlock.neighbors;
            List<WalkableBlock> existingNeighbors = Lists.newArrayList();
            for (WalkableBlock neighbor : neighbors) {
                if (neighbor != null) {
                    existingNeighbors.add(neighbor);
                }
            }
            if (existingNeighbors.size() > 0) {
                // Sorting the list of neighboring blocks based on distance from player (farthest first)
                existingNeighbors.sort((one, two) -> {
                    double a = one.getBlockPosition().distanceSquared(playerPosition);
                    double b = two.getBlockPosition().distanceSquared(playerPosition);
                    return a > b ? -1
                            : a < b ? 1
                            : 0;
                });
                // Select any of the first 4 neighboring blocks to make path random and not linear
                currentBlock = existingNeighbors.get(random.nextInt(min(4, existingNeighbors.size())));
            }
        }
        return currentBlock;
    }

}
