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
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

/**
 * Created by dsgraham on 8/27/16.
 */
public class GameObject {

    public  final float HIT_DELTA_Y = 32;
    public  final float HIT_DELTA_Z = 32;
    public  float       attack_range = 24;

    public static boolean DRAW_BOUNDS = true;

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
    public int health;
    public int attackPower = 1;
    public float respawnTimer;



    public Vector2 movePoint;
    public Vector2 direction;
    public Vector3 testPosition;

    public boolean isOverFloor;

    private static final int SHADOW_Y_OFFSET = -10;
    private static final int SHADOW_DRAW_WIDTH = 64;
    private static final int SHADOW_TEXTURE_WIDTH = 32;
    private static final int SHADOW_TEXTURE_HEIGHT = 32;
    private Rectangle shadowMasterRectangle;
    private Array<Rectangle> shadowDisplayRectangles;
    private Array<Rectangle> shadowSourceRectangles;

    public GameObject(Level level){
        tex = new TextureRegion(Assets.debugTexture, 50, 50);
        shadowTex = new TextureRegion(Assets.shadowTexture, SHADOW_TEXTURE_WIDTH, SHADOW_TEXTURE_HEIGHT);
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

        shadowMasterRectangle = new Rectangle();
        shadowDisplayRectangles = new Array<Rectangle>();
        shadowSourceRectangles = new Array<Rectangle>();
        animationTimer = new MutableFloat(0f);

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

        // Can't fall when jumping
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
        } else {
            tex = walkAnimation.getKeyFrame(0);
        }

        position.x += bounceBack.x;
        position.y += bounceBack.y;
        bounceBack.scl(0.8f);
        if (bounceBack.epsilonEquals(0.0f, 0.0f, 0.1f)) {
            bounceBack.set(0f, 0f);
        }

        // Shadows!
        // Master shadow position
        shadowMasterRectangle.set(position.x, position.y + SHADOW_Y_OFFSET, SHADOW_DRAW_WIDTH, height);
        shadowDisplayRectangles = new Array<Rectangle>();
        shadowSourceRectangles = new Array<Rectangle>();
        Rectangle r;
        // Fit to each tile in the area
        for ( Rectangle tile : tiles ) {
            if ( shadowMasterRectangle.overlaps( tile ) ) {
                r = new Rectangle();
                Intersector.intersectRectangles(tile, shadowMasterRectangle, r);
                shadowDisplayRectangles.add( r );
                shadowSourceRectangles.add( new Rectangle(
                        ((r.x - shadowMasterRectangle.x) / shadowMasterRectangle.width) * 32,
                        32 - (((r.y - shadowMasterRectangle.y) / shadowMasterRectangle.height) * 32),
                        ( r.width / shadowMasterRectangle.width ) * 32,
                        ( r.height / shadowMasterRectangle.height ) * -32
                ) );
            }

        }

    }

    public void render(SpriteBatch batch){
        if (dead) return;
        if ( ! falling ) {
            Rectangle shadowDisplayRectangle;
            Rectangle shadowSourceRectangle;
            for ( int i = 0; i < shadowDisplayRectangles.size; i++ ) {
                shadowDisplayRectangle = shadowDisplayRectangles.get(i);
                shadowSourceRectangle = shadowSourceRectangles.get(i);
                batch.draw(
                        Assets.shadowTexture,
                        shadowDisplayRectangle.x,
                        shadowDisplayRectangle.y,
                        shadowDisplayRectangle.width,
                        shadowDisplayRectangle.height,
                        Math.round(shadowSourceRectangle.x),
                        Math.round(shadowSourceRectangle.y),
                        Math.round(shadowSourceRectangle.width),
                        Math.round(shadowSourceRectangle.height),
                        false, true
                );
            }
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
        if (isAttacking || isInvulerable()) return;    // Early out if you are attacking

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


    public int doesHit(GameObject enemy) {
        if (enemy.position.y > (this.position.y - HIT_DELTA_Y)
                && enemy.position.y < (this.position.y + HIT_DELTA_Y)
                && enemy.position.z > (this.position.z - HIT_DELTA_Z)
                && enemy.position.z < (this.position.z + HIT_DELTA_Z)) {
            if (isFacingRight && (enemy.hitBounds.x > hitBounds.x) && (enemy.hitBounds.x < hitBounds.x + hitBounds.width + attack_range)) {
                return 1;
            }
            if (!isFacingRight && (enemy.hitBounds.x + enemy.hitBounds.width < hitBounds.x + hitBounds.width)
                    && (enemy.hitBounds.x + enemy.hitBounds.width > hitBounds.x - attack_range)) {
                return -1;
            }
        }
        return 0;
    }

    public void getHurt(int dmg, int dir) {
        if ((health -= dmg) <= 0) {
            if (dead) return;
            health = 0;
            dead = true;
            respawnTimer = 1.1f;
            Assets.particles.addParticle(hitBounds, Color.WHITE);
        } else {
            LudumDare36.game.wobbleScreen();
            invunerableTimer = .5f;
            invulerabilityFlashSpeed = 0.1f;
            bounceBack.set(dir * 10f, 0f);
        }
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
