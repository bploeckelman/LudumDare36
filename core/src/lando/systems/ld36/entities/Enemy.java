package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld36.levels.Level;

public class Enemy extends GameObject {
    enum ACTION {WANDER};
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
    public Vector2 movePoint;
    public Vector2 direction;


    public Enemy(Level level) {
        super(level);
        animationTimer = new MutableFloat(0f);
        health = 5;
        movePoint = new Vector2();
        direction = new Vector2();
        currentState = ACTION.WANDER;
        moveSpeed = 50;
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

        switch(currentState){
            case WANDER:
                wander(dt);
                break;
        }

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
        } else {
            isHurt = true;
            hurtCooldown = 1f;
            bounceBack.set(dir * 10f, 0f);
        }
    }

    public void wander(float dt){
        float moveLeft = moveSpeed * dt;
        while (moveLeft > 0) {
            if (movePoint.epsilonEquals(0, 0, .5f)) {
                float x = position.x + MathUtils.random(200) - 100;
                float y = position.y + MathUtils.random(200) - 100;
                y = MathUtils.clamp(y, bottomPlayArea, topPlayArea);
                x = MathUtils.clamp(x, leftEdge, level.getLevelWidth() - (width / 2));
                movePoint.set(x, y);
            }

            float dist = Vector2.dst(movePoint.x, movePoint.y, position.x, position.y);
            if (dist < moveLeft){
                position.x = movePoint.x;
                position.y = movePoint.y;
                moveLeft -= dist;
                movePoint.setZero();
            } else {
                direction.set(movePoint.cpy().sub(position.x, position.y));
                direction.nor().scl(moveLeft);
                position.add(direction.x, direction.y, 0);
                moveLeft = 0;
            }
        }
    }

}
