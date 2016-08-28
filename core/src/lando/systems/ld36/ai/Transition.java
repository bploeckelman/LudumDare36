package lando.systems.ld36.ai;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.ai.conditions.Condition;
import lando.systems.ld36.ai.states.State;

/**
 * Created by dsgraham on 8/28/16.
 */
public class Transition {
    State from;
    Condition condition;
    State to;

    public Transition(State from, Condition condition, State to){
        this.from = from;
        this.condition = condition;
        this.to = to;
    }
}
