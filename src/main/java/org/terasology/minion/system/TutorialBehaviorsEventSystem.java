// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.minion.system;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.characters.CharacterMovementComponent;
import org.terasology.minion.component.FleeComponent;
import org.terasology.minion.component.FleeOnHitComponent;
import org.terasology.module.health.events.OnDamagedEvent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class TutorialBehaviorsEventSystem extends BaseComponentSystem {

    @ReceiveEvent(components = FleeOnHitComponent.class)
    public void onDamage(OnDamagedEvent event, EntityRef entity) {

        // Make entity flee
        FleeComponent fleeComponent = new FleeComponent();
        fleeComponent.instigator = event.getInstigator();
        fleeComponent.minDistance = entity.getComponent(FleeOnHitComponent.class).minDistance;
        entity.saveComponent(fleeComponent);

        // Increase speed by multiplier factor
        CharacterMovementComponent characterMovementComponent = entity.getComponent(CharacterMovementComponent.class);
        characterMovementComponent.speedMultiplier = entity.getComponent(FleeOnHitComponent.class).speedMultiplier;
        entity.saveComponent(characterMovementComponent);

    }

}
