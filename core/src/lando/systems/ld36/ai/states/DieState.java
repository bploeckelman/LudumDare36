package lando.systems.ld36.ai.states;

import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/29/16.
 */
public class DieState extends State{
    public DieState(GameObject owner) {
        super(owner);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void onEnter() {
        owner.dead = true;
    }

    @Override
    public void onExit() {

    }
}
