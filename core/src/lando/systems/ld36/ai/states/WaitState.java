package lando.systems.ld36.ai.states;

import com.badlogic.gdx.Gdx;
import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/28/16.
 */
public class WaitState extends State {


    public WaitState(GameObject owner) {
        super(owner);
    }

    @Override
    public void update(float dt) {
         // do nothing?
    }

    @Override
    public void onEnter() {
        Gdx.app.log("STATE-MACHINE", "Enter Wait");
    }

    @Override
    public void onExit() {
        Gdx.app.log("STATE-MACHINE", "Exit Wait");
    }
}
