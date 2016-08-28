package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.ai.Transition;
import lando.systems.ld36.ai.conditions.NearScreenCondition;
import lando.systems.ld36.ai.states.ChaseState;
import lando.systems.ld36.ai.states.WaitState;
import lando.systems.ld36.ai.states.WanderState;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class Enemy extends GameObject {

    /**
     * Simple State Machine for Enemies
     */
    enum ACTION {
        /**
         * Wander around aimlessly
         */
        WANDER,
        /**
         * Chase the player
         */
        CHASE,
        /**
         * Wait until they are just on screen
         */
        WAIT}
    public boolean isAttacking = false;
    public boolean isMoving = false;
    public boolean isHurt = false;
    public MutableFloat animationTimer;
    public float timer = 0f;
    public float hurtCooldown = 0f;
    public int health;
    public Animation walkAnimation;
    public Animation attackAnimation;
    public ACTION currentState;
    public StateMachine stateMachine;



    public Enemy(Level level) {
        super(level);
        animationTimer = new MutableFloat(0f);
        health = 5;

        currentState = ACTION.CHASE;
        moveSpeed = 50;

        initializeStates();

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

    public void update(float dt) {
        super.update(dt);

        if (isAttacking) {
            tex = attackAnimation.getKeyFrame(animationTimer.floatValue());
        }
        else if (isMoving) {
            tex = walkAnimation.getKeyFrame(timer);
        }

        if (isHurt) {
            if ((hurtCooldown -= dt) <= 0f) {
                hurtCooldown = 0f;
                isHurt = false;
            }
        }

        stateMachine.update(dt);

        isFacingRight = direction.x > 0;

        position.x += bounceBack.x;
        position.y += bounceBack.y;
        bounceBack.scl(0.8f);
        if (bounceBack.epsilonEquals(0.0f, 0.0f, 0.1f)) {
            bounceBack.set(0f, 0f);
        }
    }

    public void getHurt(int dmg, int dir) {
        if ((health -= dmg) <= 0) {
            dead = true;
            Assets.particles.addParticle(hitBounds, Color.WHITE);
        } else {
            LudumDare36.game.wobbleScreen();
            isHurt = true;
            hurtCooldown = 1f / dmg;
            invunerableTimer = hurtCooldown;
            invulerabilityFlashSpeed = 0.1f;
            bounceBack.set(dir * 10f, 0f);
        }
    }


}
