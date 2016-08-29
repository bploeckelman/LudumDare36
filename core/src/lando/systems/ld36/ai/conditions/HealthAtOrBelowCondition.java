package lando.systems.ld36.ai.conditions;

import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/29/16.
 */
public class HealthAtOrBelowCondition extends Condition {
    public int health;

    public HealthAtOrBelowCondition(GameObject owner, int health) {
        super(owner);
        this.health = health;
    }

    @Override
    public boolean isTrue() {
        return owner.health <= health;
    }
}
