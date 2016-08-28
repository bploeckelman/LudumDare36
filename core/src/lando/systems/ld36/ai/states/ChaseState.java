package lando.systems.ld36.ai.states;

import com.badlogic.gdx.Gdx;
import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public class ChaseState extends State {
    GameObject owner;

    public ChaseState(GameObject owner){
        this.owner = owner;
    }

    @Override
    public void update(float dt) {
        float moveLeft = owner.moveSpeed * dt;
        owner.movePoint.set(owner.level.player.position.x, owner.level.player.position.y);
        moveLeft = owner.updateMove(dt, moveLeft);
    }

    @Override
    public void onEnter() {
        Gdx.app.log("STATE-MACHINE", "Enter Chase");
    }

    @Override
    public void onExit() {
        Gdx.app.log("STATE-MACHINE", "Exit Chase");

    }
}
