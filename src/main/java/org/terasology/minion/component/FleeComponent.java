// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.minion.component;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * This component is used by the FleeOnHit and FleeInProximity module to allow an
 * NPC to exhibit the Flee behavior
 */
public class FleeComponent implements Component<FleeComponent> {
    /* Minimum distance from instigator after which the NPC will stop 'flee'ing */
    public float minDistance = 10f;
    /* Entity to run away from */
    public EntityRef instigator;

    @Override
    public void copyFrom(FleeComponent other) {
        this.minDistance = other.minDistance;
        this.instigator = other.instigator;
    }
}
