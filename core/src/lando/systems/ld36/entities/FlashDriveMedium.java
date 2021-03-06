package lando.systems.ld36.entities;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.ai.Transition;
import lando.systems.ld36.ai.conditions.AwayFromObjectCondition;
import lando.systems.ld36.ai.conditions.NearObjectCondition;
import lando.systems.ld36.ai.conditions.NearScreenCondition;
import lando.systems.ld36.ai.states.*;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class FlashDriveMedium extends FlashDriveEasy {
    public FlashDriveMedium(Level level, float x, float y) {
        super(level, x, y);

        walkAnimation = Assets.flashWalkMedium;
        attackAnimation = Assets.flashPunchMedium;

        tex = walkAnimation.getKeyFrame(timer);

        health = 5;
        maxHealth = 5;
        attackPower = 2;
        moveSpeed = 75;
    }

    public void initializeStates(){
        // States enemy can have
        WaitState wait = new WaitState(this);
        WanderState wander = new WanderState(this);
        ChaseAvoidState chase = new ChaseAvoidState(this);

        // Conditions
        NearScreenCondition nearCond = new NearScreenCondition(level.screen.camera, this);
        NearObjectCondition nearPlayer = new NearObjectCondition(this, level.player, 200);
        AwayFromObjectCondition farPlayer = new AwayFromObjectCondition(this, level.player, 200);

        // Transitions
        Array<Transition> transitions = new Array<Transition>();
        transitions.add(new Transition(wait, nearCond, wander));
        transitions.add(new Transition(wander, nearPlayer, chase));
        transitions.add(new Transition(chase, farPlayer, wander));

        // Create State Machine
        stateMachine = new StateMachine(wait, transitions);
        addDieState();

    }
}
