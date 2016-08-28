package lando.systems.ld36.ai.states;

import com.badlogic.gdx.Gdx;

/**
 * Created by dsgraham on 8/28/16.
 */
public class WaitState extends State {


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
