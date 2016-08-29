package lando.systems.ld36.ai.conditions;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/29/16.
 */
public class EndOfScreenCondition extends Condition {
    OrthographicCamera camera;

    public EndOfScreenCondition(GameObject owner) {
        super(owner);
        camera = owner.level.screen.camera;
    }

    @Override
    public boolean isTrue() {
        return MathUtils.isEqual(owner.level.screen.cameraCenter.x, owner.level.getLevelWidth() - (camera.viewportWidth / 2), 10);
    }
}
