package lando.systems.ld36.ai.states;

import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/29/16.
 */
public class FloatUpState extends State {

    public FloatUpState(GameObject owner) {
        super(owner);
    }

    @Override
    public void update(float dt) {
        owner.position.z += 50*dt;
        owner.position.z = Math.min(owner.position.z, 100);
    }

    @Override
    public void onEnter() {
        owner.invunerableTimer = 2f;
    }

    @Override
    public void onExit() {

    }
}
