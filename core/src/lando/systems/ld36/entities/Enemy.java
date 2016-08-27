package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Enemy extends GameObject {
    public boolean isAttacking = false;
    public boolean isMoving = false;
    public MutableFloat animationTimer;
    public float timer = 0f;
    public Animation walkAnimation;
    public Animation attackAnimation;

    public Enemy() {
        animationTimer = new MutableFloat(0f);
    }

    public void update(float dt) {
        super.update(dt);

        if (isAttacking) {
            tex = attackAnimation.getKeyFrame(animationTimer.floatValue());
        }
        else if (isMoving) {
            tex = walkAnimation.getKeyFrame(timer);
        }
    }
}
