package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.ai.Transition;
import lando.systems.ld36.ai.conditions.AwayFromObjectCondition;
import lando.systems.ld36.ai.conditions.NearObjectCondition;
import lando.systems.ld36.ai.conditions.NearScreenCondition;
import lando.systems.ld36.ai.states.ChaseAvoidState;
import lando.systems.ld36.ai.states.ChaseState;
import lando.systems.ld36.ai.states.WaitState;
import lando.systems.ld36.ai.states.WanderState;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class SDEasy extends Enemy {
    public SDEasy(Level level, float x, float y) {
        super(level);
        walkAnimation = Assets.sdWalk;
        attackAnimation = Assets.sdAttack;

        tex = walkAnimation.getKeyFrame(timer);

        width = tex.getRegionWidth();
        height = new MutableFloat(tex.getRegionHeight());

        jumpVelocity = 50f;
        hitBounds = new Rectangle(position.x, position.y, 32f, tex.getRegionHeight());

        health = 2;
        maxHealth = 2;
        position.x = x;
        position.y = y;

        // TODO: Maybe remove this?
        isMoving = true;
    }

    public void update(float dt) {
        super.update(dt);
        jump();
        timer += dt;

    }

    public void initializeStates(){
        // States enemy can have
        WaitState wait = new WaitState(this);
        WanderState wander = new WanderState(this);
        ChaseAvoidState chase = new ChaseAvoidState(this);

        // Conditions
        NearScreenCondition nearCond = new NearScreenCondition(level.screen.camera, this);
        NearObjectCondition nearPlayer = new NearObjectCondition(this, level.player, 70);
        AwayFromObjectCondition farPlayer = new AwayFromObjectCondition(this, level.player, 70);

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
