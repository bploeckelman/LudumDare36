package lando.systems.ld36.ai.conditions;

import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public class AwayFromObjectCondition extends Condition{
    GameObject other;
    float distance;

    public AwayFromObjectCondition(GameObject owner, GameObject other, float distance) {
        super(owner);
        this.other = other;
        this.distance = distance;
    }

    @Override
    public boolean isTrue() {
        return owner.position.dst(other.position) > distance;
    }
}
