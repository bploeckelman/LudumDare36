package lando.systems.ld36.entities;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.ai.Transition;
import lando.systems.ld36.ai.conditions.BossDeadCondition;
import lando.systems.ld36.ai.conditions.NearScreenCondition;
import lando.systems.ld36.ai.states.ChaseAvoidState;
import lando.systems.ld36.ai.states.WaitState;
import lando.systems.ld36.ai.states.WanderState;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

/**
 * Created by dsgraham on 8/29/16.
 */
public class Cassette extends GameObject {
    public Cassette(Level level, float x, float y) {
        super(level);

        walkAnimation = Assets.cassetteWalking;
        attackAnimation = Assets.cassetteWalking;

        tex = walkAnimation.getKeyFrame(timer);

        width = tex.getRegionWidth();
        height = tex.getRegionHeight();

        jumpVelocity = 50f;
        position.x = x;
        position.y = y;
    }

    public void initializeStates() {
        WaitState wait = new WaitState(this);

        // Conditions
        NearScreenCondition nearCond = new NearScreenCondition(level.screen.camera, this);
        BossDeadCondition bossDead = new BossDeadCondition(this);

        // Transitions
        Array<Transition> transitions = new Array<Transition>();
//        transitions.add(new Transition(wait, bossDead, chase));

        // Create State Machine
        stateMachine = new StateMachine(wait, transitions);
    }

    public void update(float dt){

        stateMachine.update(dt);

    }


}
