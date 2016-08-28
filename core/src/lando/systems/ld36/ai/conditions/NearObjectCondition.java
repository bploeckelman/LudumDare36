package lando.systems.ld36.ai.conditions;

import com.badlogic.gdx.Game;
import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public class NearObjectCondition extends Condition {
    GameObject other;
    float distance;

    public NearObjectCondition(GameObject owner, GameObject other, float distance){
        super(owner);
        this.other = other;
        this.distance = distance;
    }

    @Override
    public boolean isTrue() {
        return owner.position.dst(other.position) <= distance;
    }
}
