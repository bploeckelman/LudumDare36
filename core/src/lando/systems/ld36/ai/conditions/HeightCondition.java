package lando.systems.ld36.ai.conditions;

import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/29/16.
 */
public class HeightCondition extends Condition {
    public float height;
    public HeightCondition(GameObject owner, float height) {
        super(owner);
        this.height = height;
    }

    @Override
    public boolean isTrue() {
        return height == owner.height.floatValue();
    }
}
