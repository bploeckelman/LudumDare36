package lando.systems.ld36.entities;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.KeyMapping;

/**
 * Created by dsgraham on 8/27/16.
 */
public class Player extends GameObject {
    public final float moveSpeed = 100;
    public boolean isMoving = false;
    public boolean isAttacking = false;
    public float timer = 0f;
    public MutableFloat animationTimer;
    public Animation walkAnimation;
    public Animation attackAnimation;
    public boolean isFacingRight;

    public Player(){
        animationTimer = new MutableFloat(0f);
        walkAnimation = Assets.floppyWalk;
        tex = walkAnimation.getKeyFrame(timer);

        attackAnimation = Assets.floppyPunch;
        isFacingRight = true;
    }

    public void update(float dt){
        super.update(dt);

        timer += dt;
        isMoving = false;

        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.JUMP) && isOnGround()){
            jump();
        }

        // TODO: Don't allow movement off of the screen;
        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.RIGHT) &&
                Assets.keyMapping.isActionPressed(KeyMapping.ACTION.LEFT)){
            // Do nothing
        } else if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.RIGHT)){
            position.x += moveSpeed * dt;
            isMoving = true;
            isFacingRight = true;
        } else if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.LEFT)){
            position.x -= moveSpeed * dt;
            isMoving = true;
            isFacingRight = false;
        }

        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.UP)){
            position.y += moveSpeed * dt;
            isMoving = true;
        }
        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.DOWN)){
            position.y -= moveSpeed * dt;
            isMoving = true;
        }

        if(Assets.keyMapping.isActionPressed(KeyMapping.ACTION.ATTACK) && !isAttacking) {
            isAttacking = true;
            animationTimer.setValue(0f);
            Tween.to(animationTimer, -1, attackAnimation.getAnimationDuration())
                .target(attackAnimation.getAnimationDuration())
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        isAttacking = false;
                    }
                })
                .start(Assets.tween);

        }

        if (isAttacking) {
            tex = attackAnimation.getKeyFrame(animationTimer.floatValue());
        }
        else if (isMoving) {
            tex = walkAnimation.getKeyFrame(timer);
        }

        if (isFacingRight && tex.isFlipX()) {
            tex.flip(true, false);
        } else if (!isFacingRight && !tex.isFlipX()) {
            tex.flip(true, false);
        }
    }
}
