package lando.systems.ld36.ai.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public class WanderState extends State{
    GameObject owner;

    public WanderState(GameObject owner){
        this.owner = owner;

    }

    @Override
    public void update(float dt) {
        float moveLeft = owner.moveSpeed * dt;
        while (moveLeft > 0) {
            if (owner.movePoint.epsilonEquals(0, 0, .5f)) {
                float x = owner.position.x + MathUtils.random(200) - 100;
                float y = owner.position.y + MathUtils.random(200) - 100;
                y = MathUtils.clamp(y, owner.bottomPlayArea, owner.topPlayArea);
                x = MathUtils.clamp(x, owner.leftEdge, owner.level.getLevelWidth() - (owner.width / 2));
                owner.movePoint.set(x, y);
            }

            moveLeft = owner.updateMove(dt, moveLeft);
        }
    }

    @Override
    public void onEnter() {
        Gdx.app.log("STATE-MACHINE", "Enter Wander");
    }

    @Override
    public void onExit() {
        Gdx.app.log("STATE-MACHINE", "Exit Wander");

    }

}
