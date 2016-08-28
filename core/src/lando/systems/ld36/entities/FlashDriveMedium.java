package lando.systems.ld36.entities;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.ai.Transition;
import lando.systems.ld36.ai.conditions.NearScreenCondition;
import lando.systems.ld36.ai.states.*;
import lando.systems.ld36.levels.Level;

public class FlashDriveMedium extends FlashDriveEasy {
    public FlashDriveMedium(Level level, float x, float y) {
        super(level, x, y);

        health = 5;
    }

    public void initializeStates(){
        // States enemy can have
        WaitState wait = new WaitState();
        WanderState wander = new WanderState(this);
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
