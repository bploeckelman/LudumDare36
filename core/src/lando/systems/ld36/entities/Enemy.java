package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.Animation;
import lando.systems.ld36.levels.Level;

public class Enemy extends GameObject {
    public boolean isAttacking = false;
    public boolean isMoving = false;
    public boolean isHurt = false;
    public boolean isDead = false;
    public MutableFloat animationTimer;
    public float timer = 0f;
    public float hurtCooldown = 0f;
    public int health;
    public Animation walkAnimation;
    public Animation attackAnimation;

    public Enemy(Level level) {
        super(level);
        animationTimer = new MutableFloat(0f);
        health = 1;
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
    }

    public void getHurt(int dmg) {
        if ((health -= dmg) <= 0) {
            isDead = true;
        } else {
            isHurt = true;
            hurtCooldown = 1f;
        }
    }

}
