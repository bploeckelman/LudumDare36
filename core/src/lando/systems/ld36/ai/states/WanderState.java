package lando.systems.ld36.ai.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public class WanderState extends State{

    public WanderState(GameObject owner){
        super(owner);

    }

    @Override
    public void update(float dt) {
        float moveLeft = owner.moveSpeed * dt;
        while (moveLeft > 0) {
            if (owner.movePoint.epsilonEquals(0, 0, .5f)) {
                float x = owner.position.x + MathUtils.random(500) - 250;
                float y = owner.position.y + MathUtils.random(200) - 100;
                y = MathUtils.clamp(y, owner.bottomPlayArea, owner.topPlayArea);
                x = MathUtils.clamp(x, owner.leftEdge, owner.level.getLevelWidth() - (owner.width / 2));
                owner.movePoint.set(x, y);
            }
            owner.movePoint.x = MathUtils.clamp(owner.movePoint.x,
                                                owner.level.screen.cameraCenter.x - owner.level.screen.camera.viewportWidth/2,
                                                owner.level.screen.cameraCenter.x + owner.level.screen.camera.viewportWidth/2 - owner.width);
            moveLeft = owner.updateMove(dt, moveLeft);
        }
    }

    @Override
    public void onEnter() {
    }

    @Override
    public void onExit() {

    }

}
