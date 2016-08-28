package lando.systems.ld36.ai.states;

/**
 * Created by dsgraham on 8/28/16.
 */
public abstract class State {
    public abstract void update(float dt);
    public abstract void onEnter();
    public abstract void onExit();

}
