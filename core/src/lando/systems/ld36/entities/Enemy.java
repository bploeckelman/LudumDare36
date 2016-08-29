package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.ai.Transition;
import lando.systems.ld36.ai.conditions.NearScreenCondition;
import lando.systems.ld36.ai.states.ChaseAvoidState;
import lando.systems.ld36.ai.states.ChaseState;
import lando.systems.ld36.ai.states.WaitState;
import lando.systems.ld36.ai.states.WanderState;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Statistics;

public class Enemy extends GameObject {

    public boolean isMoving = false;
    public MutableFloat animationTimer;
    public float timer = 0f;


    public StateMachine stateMachine;



    public Enemy(Level level) {
        super(level);
        animationTimer = new MutableFloat(0f);
        health = 5;
        maxHealth = 5;

        moveSpeed = 50;


    }

    public void initializeStates(){
        // States enemy can have
        WaitState wait = new WaitState(this);
        WanderState wander = new WanderState(this);
        ChaseAvoidState chase = new ChaseAvoidState(this);

        // Conditions
        NearScreenCondition nearCond = new NearScreenCondition(level.screen.camera, this);

        // Transitions
        Array<Transition> transitions = new Array<Transition>();
        transitions.add(new Transition(wait, nearCond, chase));

        // Create State Machine
        stateMachine = new StateMachine(wait, transitions);
    }

    public void update(float dt) {
        super.update(dt);

        stateMachine.update(dt);
        isFacingRight = direction.x > 0;

        tryAttack();

        hitBounds.x = position.x + 15f;
        hitBounds.y = position.y + position.z;

    }

    public void tryAttack(){
        // Lets make them not always try to hit you
        if (MathUtils.random() < .97f) return;
        if (position.dst(level.player.position) < 100){
            attack();
        }

    }

    public void getHurt(int dmg, int dir) {
        Statistics.damageDealt += dmg;
        super.getHurt(dmg, dir);
        if (dead) {
            Statistics.enemiesKilled++;
        }
    }


}
