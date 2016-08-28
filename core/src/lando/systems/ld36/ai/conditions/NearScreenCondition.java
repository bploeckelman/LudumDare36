package lando.systems.ld36.ai.conditions;

import com.badlogic.gdx.graphics.OrthographicCamera;
import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public class NearScreenCondition extends Condition {
    OrthographicCamera camera;

    public NearScreenCondition(OrthographicCamera camera, GameObject owner){
        super(owner);
        this.camera = camera;
    }


    @Override
    public boolean isTrue() {
        return camera.position.x + (camera.viewportWidth/2) > owner.position.x;
    }
}
