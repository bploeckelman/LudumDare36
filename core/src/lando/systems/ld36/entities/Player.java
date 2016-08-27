package lando.systems.ld36.entities;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.KeyMapping;

/**
 * Created by dsgraham on 8/27/16.
 */
public class Player extends GameObject {
    public final float moveSpeed = 150;
    public final float bottomPlayArea = 0;
    public final float topPlayArea = 5.5f * 32;
    public boolean isMoving = false;
    public boolean isAttacking = false;
    public float timer = 0f;
    public MutableFloat animationTimer;
    public Animation walkAnimation;
    public Animation attackAnimation;
    public Rectangle footBounds;

    Array<Rectangle> tiles;

    public Player(){
        tiles = new Array<Rectangle>();
        animationTimer = new MutableFloat(0f);
        walkAnimation = Assets.floppyWalk;
        tex = walkAnimation.getKeyFrame(timer);

        width = tex.getRegionWidth();
        height = tex.getRegionHeight();

        attackAnimation = Assets.floppyPunch;
        isFacingRight = true;
        footBounds = new Rectangle();
    }

    public void update(float dt, float leftEdge, Level level){
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

        // Keep player on play area
        position.y = MathUtils.clamp(position.y, bottomPlayArea, topPlayArea);
        position.x = MathUtils.clamp(position.x, leftEdge, level.getLevelWidth() - (width/2)) ;

        level.getGroundTiles(position, tiles);
        footBounds.set(position.x + 10, position.y, 45, 5);
        boolean falling = true;
        for (Rectangle tile : tiles){
            if (footBounds.overlaps(tile) || tile.contains(footBounds)){
                falling = false;
                break;
            }
        }

        // Can't fall when when jumping
        if (position.z > 0){
            falling = false;
        }

        if (falling){
            System.out.println("Falling");
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
    }

    public void render(SpriteBatch batch){
        super.render(batch);

    }
}
