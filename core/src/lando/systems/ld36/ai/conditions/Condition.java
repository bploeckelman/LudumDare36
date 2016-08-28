package lando.systems.ld36.ai.conditions;

import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public abstract class Condition {
    GameObject owner;

    public Condition(GameObject owner){
        this.owner = owner;;
    }

    // find a way to make this a condition check

    public abstract boolean isTrue();
}
