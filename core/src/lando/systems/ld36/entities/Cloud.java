package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.ai.Transition;
import lando.systems.ld36.ai.conditions.*;
import lando.systems.ld36.ai.states.*;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class Cloud extends Enemy {

    public float shootDelay;
    public Array<CloudProjectile> projectiles;

    public Cloud(Level level, float x, float y, boolean isBoss) {
        super(level);

        position.x = x;
        position.y = y;

        walkAnimation = Assets.cloud;
        attackAnimation = Assets.cloud;

        tex = walkAnimation.getKeyFrame(timer);
        hitBounds = new Rectangle(position.x, position.y, 128f, tex.getRegionHeight());

        width = tex.getRegionWidth();
        height = new MutableFloat(0);

        isMoving = true;
        characterSpriteWidth = 128;
        shadowDrawWidth = 128;
        shadowYOffset = -15;
        health = maxHealth = 100;
        floating = true;

        if (isBoss) level.boss = this;
        projectiles = new Array<CloudProjectile>();
        name = "\"The Cloud\"";
        canWalkOnWall = true;
        showHealthBar = false;
    }

    public void update(float dt) {
        super.update(dt);
        verticalVelocity = 0; // cloud floats
        hitBounds.x = position.x;
        hitBounds.y = position.y + position.z;
        for (int i = projectiles.size -1; i >= 0; i--){
            CloudProjectile proj = projectiles.get(i);
            proj.update(dt);
            OrthographicCamera cam = level.screen.camera;
            if (proj.position.x + proj.hitBox.width < cam.position.x - cam.viewportWidth /2 ||
                    proj.position.x > cam.position.x + cam.viewportWidth /2 ||
                    proj.position.y + proj.hitBox.height < cam.position.y - cam.viewportHeight/2 ||
                    proj.position.y > cam.position.y + cam.viewportHeight /2){
                projectiles.removeIndex(i);
            }
            if (proj.hitBox.overlaps(level.player.hitBounds)){
                projectiles.removeIndex(i);
                int dir =proj.velocity.x < 0 ? -1 : 1;
                Assets.particles.addBloodParticles(level.player.hitBounds, dir, proj.damage);
                level.player.getHurt(proj.damage, dir);
            }
        }
    }

    public void initializeStates() {

        // States enemy can have
        WaitState wait = new WaitState(this);
        GrowState grow = new GrowState(this,tex.getRegionHeight());
        ChaseState chase = new ChaseState(this);
        FloatUpState floatUp = new FloatUpState(this);
        CloudStage1State stage1 = new CloudStage1State(this);
        CloudStage2State stage2 = new CloudStage2State(this);
        CloudStage3State stage3 = new CloudStage3State(this);

        // Conditions
        EndOfScreenCondition endOfScreen = new EndOfScreenCondition(this);
        HeightCondition heightCond = new HeightCondition(this, tex.getRegionHeight());
        HealthAtOrBelowCondition healthBelow75 = new HealthAtOrBelowCondition(this, 75);
        HealthAtOrBelowCondition healthBelow50 = new HealthAtOrBelowCondition(this, 50);
        HealthAtOrBelowCondition healthBelow25 = new HealthAtOrBelowCondition(this, 25);
        FloatingCondition floatingCondition = new FloatingCondition(this);


        // Transitions
        Array<Transition> transitions = new Array<Transition>();
//        transitions.add(new Transition(wait, nearCond, stage3));
        transitions.add(new Transition(wait, endOfScreen, grow));
        transitions.add(new Transition(grow, heightCond, chase));
        transitions.add(new Transition(chase, healthBelow75, floatUp));
        transitions.add(new Transition(floatUp, floatingCondition, stage1));
        transitions.add(new Transition(stage1, healthBelow50, stage2));
        transitions.add(new Transition(stage2, healthBelow25, stage3));

        // Create State Machine
        stateMachine = new StateMachine(wait, transitions);
    }

    public void render(SpriteBatch batch){
        isFacingRight = true;
        super.render(batch);
        for (CloudProjectile proj : projectiles){
            proj.render(batch);
        }
    }
}
