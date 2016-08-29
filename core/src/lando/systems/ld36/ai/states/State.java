package lando.systems.ld36.ai.states;

import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public abstract class State {
    GameObject owner;

    public State(GameObject owner){
        this.owner = owner;
    }

    public abstract void update(float dt);
    public abstract void onEnter();
    public abstract void onExit();

}
