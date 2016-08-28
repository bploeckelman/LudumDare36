package lando.systems.ld36.entities;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.ai.Transition;
import lando.systems.ld36.ai.conditions.AwayFromObjectCondition;
import lando.systems.ld36.ai.conditions.NearObjectCondition;
import lando.systems.ld36.ai.conditions.NearScreenCondition;
import lando.systems.ld36.ai.states.ChaseState;
import lando.systems.ld36.ai.states.WaitState;
import lando.systems.ld36.ai.states.WanderState;
import lando.systems.ld36.levels.Level;

public class FlashDriveHard extends FlashDriveMedium {
    public FlashDriveHard(Level level, float x, float y) {
        super(level, x, y);

        health = 10;
    }

    public void initializeStates(){
        // States enemy can have
        WaitState wait = new WaitState();
        ChaseState chase = new ChaseState(this);

        // Conditions
        NearScreenCondition nearCond = new NearScreenCondition(level.screen.camera, this);


        // Transitions
        Array<Transition> transitions = new Array<Transition>();
        transitions.add(new Transition(wait, nearCond, chase));


        // Create State Machine
        stateMachine = new StateMachine(wait, transitions);
    }
}
