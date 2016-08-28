package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld36.levels.Level;

public class Enemy extends GameObject {
    public Vector2 bounceBack;
    public boolean isAttacking = false;
    public boolean isMoving = false;
    public boolean isHurt = false;
    public MutableFloat animationTimer;
    public float timer = 0f;
    public float hurtCooldown = 0f;
    public int health;
    public Animation walkAnimation;
    public Animation attackAnimation;

    public Enemy(Level level) {
        super(level);
        animationTimer = new MutableFloat(0f);
        health = 5;
        bounceBack = new Vector2();
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

}
