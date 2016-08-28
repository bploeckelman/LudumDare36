package lando.systems.ld36.entities;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

/**
 * Created by dsgraham on 8/27/16.
 */
public class GameObject {

    public static boolean DRAW_BOUNDS = false;

    public final float INVULERABLITIYDELAY = 3f;
    public final float GRAVITY = -200;
    public float jumpVelocity = 200;
    public float moveSpeed;


    public final float bottomPlayArea = 0;
    public final float topPlayArea = 5.5f * 32;
    public Vector3 position;
    public Rectangle hitBounds;
    public float verticalVelocity;
    public TextureRegion tex;
    public TextureRegion shadowTex;
    public boolean isFacingRight = true;
    public float width;
    public float height;
    public boolean falling;
    public Level level;
    Array<Rectangle> tiles;
    public Rectangle footBounds;
    public float leftEdge;
    public int jumpCount;
    public boolean dead;
    public Array<Vector2> lastSafePlace;
    public float invunerableTimer;
    public Vector2 bounceBack;
    public float invulerabilityFlashSpeed = .5f;
    public boolean isMoving = false;
    public boolean isAttacking = false;
    public Animation walkAnimation;
    public Animation attackAnimation;
    public MutableFloat animationTimer;
    public float timer = 0f;


    public Vector2 movePoint;
    public Vector2 direction;
    public Vector3 testPosition;



    public GameObject(Level level){
        tex = new TextureRegion(Assets.debugTexture, 50, 50);
        shadowTex = new TextureRegion(Assets.shadowTexture, 32, 32);
        position = new Vector3();
        width = 50;
        height = width;
        this.level = level;
        hitBounds = new Rectangle(position.x, position.y, tex.getRegionWidth(), tex.getRegionHeight());
        tiles = new Array<Rectangle>();
        footBounds = new Rectangle();
        jumpCount = 0;
        lastSafePlace = new Array<Vector2>();
        bounceBack = new Vector2();
        movePoint = new Vector2();
        direction = new Vector2();
        testPosition = new Vector3();
    }

    public void initializeStates() {
    }

    public void update(float dt){
        timer += dt;

        if (invunerableTimer > 0){
            invunerableTimer -= dt;
            if (invunerableTimer<=0){
                invunerableTimer = 0;
            }
        }
        // Keep player on play area
        position.y = MathUtils.clamp(position.y, bottomPlayArea, topPlayArea);
        position.x = MathUtils.clamp(position.x, leftEdge, level.getLevelWidth() - (width/2)) ;

        falling = notSafeToWalk(position);


        // Jumping
        position.z += verticalVelocity * dt;
        if (!falling && isOnGround()){
            jumpCount = 0;
            verticalVelocity = 0;
            lastSafePlace.add(new Vector2(position.x, position.y));
            if (lastSafePlace.size>60){
                lastSafePlace.removeIndex(0);
            }
        } else {
            verticalVelocity += GRAVITY * dt;
        }

        // Can't fall when  jumping
        if (position.z > 0){
            falling = false;
        }

        if (position.y + position.z < -height){
            dead = true;
        }

        if (isAttacking) {
            tex = attackAnimation.getKeyFrame(animationTimer.floatValue());
        }
        else if (isMoving) {
            tex = walkAnimation.getKeyFrame(timer);
        }
    }

    public void render(SpriteBatch batch){
        if (!falling) {
            batch.draw(shadowTex, position.x, position.y - 10, 64, height);
        }

        // Flash the thing if it is invulnerable
        float alpha = 1;
        if (invunerableTimer > 0){
            alpha = (invunerableTimer % invulerabilityFlashSpeed) * (1/ invulerabilityFlashSpeed);
        }
        batch.setColor(1,1,1,alpha);
        if (isFacingRight) {
            batch.draw(tex, position.x, position.y + position.z, width, height);
        } else {
            batch.draw(tex, position.x + (width/1.5f), position.y + position.z, -width, height);
        }
        batch.setColor(Color.WHITE);
        if (DRAW_BOUNDS) {
            batch.end();
            Assets.shapes.setColor(Color.RED);
            Assets.shapes.begin(ShapeRenderer.ShapeType.Line);
            Assets.shapes.rect(hitBounds.x, hitBounds.y, hitBounds.width, hitBounds.height);
            Assets.shapes.end();
            batch.begin();
        }
    }


    public boolean isOnGround(){
        if (position.z < 0){
            position.z = 0;
        }
        return position.z <= 0;
    }

    public void jump(){
        if (jumpCount > 0) return;
        jumpCount++;
        verticalVelocity = jumpVelocity;
        position.z += .01f;
    }

    public void attack(){
        if (isAttacking) return;    // Early out if you are attacking

        animationTimer.setValue(0f);
        Timeline.createSequence()
                .push(Tween.to(animationTimer, -1, attackAnimation.getAnimationDuration() /3f)
                      .target(attackAnimation.getAnimationDuration()/3f))
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        isAttacking = true;  // Start checking for attacks hitting a third of the way through the animation
                    }
                }))
                .push(Tween.to(animationTimer, -1, attackAnimation.getAnimationDuration() /3f)
                        .target(attackAnimation.getAnimationDuration() / 3f * 2f))
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        isAttacking = false; // Stop checking after 2/3s of the animation are completed
                    }
                }))
                .push(Tween.to(animationTimer, -1, attackAnimation.getAnimationDuration() / 3f)
                    .target(attackAnimation.getAnimationDuration()))
                .start(Assets.tween);

    }

    public boolean isInvulerable(){
        return invunerableTimer > 0;
    }


    public boolean notSafeToWalk(Vector3 pos){
        level.getGroundTiles(pos, tiles);
        footBounds.set(pos.x + 10, pos.y, 45, 5);
        boolean willFall = true;
        for (Rectangle tile : tiles){
            if (pos.z >= 0 && footBounds.overlaps(tile) || tile.contains(footBounds)){
                willFall = false;
                break;
            }
        }
        return  willFall;
    }

    public float updateMove(float dt, float moveLeft){
        float dist = Vector2.dst(movePoint.x, movePoint.y, position.x, position.y);
        isMoving = dist > 0;
        if (dist < moveLeft){
            testPosition.set(movePoint.x, movePoint.y, 0);
            if (!notSafeToWalk(testPosition)) {
                position.x = movePoint.x;
                position.y = movePoint.y;
            }
            moveLeft -= dist;
            movePoint.setZero();
        } else {
            direction.set(movePoint.cpy().sub(position.x, position.y));
            direction.nor().scl(moveLeft);
            testPosition.set(position);
            testPosition.add(direction.x, direction.y, 0);
            if (notSafeToWalk(testPosition)) {
                movePoint.setZero();
            } else {
                position.set(testPosition);
            }
            moveLeft = 0;
        }
        return moveLeft;
    }
}
