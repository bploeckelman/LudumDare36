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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.ai.StateMachine;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Sounds;

/**
 * Created by dsgraham on 8/27/16.
 */
public class GameObject {

    public  final float HIT_DELTA_Y = 32;
    public  final float HIT_DELTA_Z = 32;
    public  float       attack_range = 24;

    public static boolean DRAW_BOUNDS = false;

    public final float RESPAWNDELAY = 4f;
    public final float GRAVITY = -200;
    public float ATTACK_COOLDOWN = .6f;
    public float jumpVelocity = 200;
    public float moveSpeed;
    public boolean floating;


    public final float bottomPlayArea = 0;
    public float topPlayArea;
    public Vector3 position;
    public Rectangle hitBounds;
    public float verticalVelocity;
    public TextureRegion tex;
    public TextureRegion shadowTex;
    public boolean isFacingRight = true;
    public float width;
    public MutableFloat height;
    public boolean falling;
    public Level level;
    Array<Rectangle> tiles;
    public Rectangle footBounds;
    public float leftEdge;
    public float rightEdge;
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
    public int maxHealth;
    public int attackPower = 1;
    public float respawnTimer;
    public float attackCooldown;
    public float characterSpriteWidth;
    public String name;

    public StateMachine stateMachine;

    public String taunt;
    public float speechBubbleTimer;
    public String speechText;


    public Vector2 movePoint;
    public Vector2 direction;
    public Vector3 testPosition;

    public boolean isOverFloor;

    private static final int SHADOW_TEXTURE_WIDTH = 32;
    private static final int SHADOW_TEXTURE_HEIGHT = 32;
    public int shadowDrawWidth = 64;
    public int shadowYOffset = -10;
    public boolean activated;
    public boolean canWalkOnWall;
    public boolean showHealthBar;

    public Color tintColor;

    private Rectangle shadowMasterRectangle;
    private Array<Rectangle> shadowDisplayRectangles;
    private Array<Rectangle> shadowSourceRectangles;

    public GameObject(Level level){
        tex = new TextureRegion(Assets.debugTexture, 50, 50);
        shadowTex = new TextureRegion(Assets.shadowTexture, SHADOW_TEXTURE_WIDTH, SHADOW_TEXTURE_HEIGHT);
        position = new Vector3();
        width = 50;
        height = new MutableFloat(width);
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
        attackCooldown = 0;
        characterSpriteWidth = 45;
        name = "Boss Name is Unset";
        taunt = "Taunt is Unset";
        tintColor = Color.WHITE;
        floating = false;
        canWalkOnWall = false;
        showHealthBar = true;
    }

    public void initializeStates() {
    }

    public void update(float dt){
        timer += dt;
        topPlayArea = level.getTopBound(position.x);

        if (invunerableTimer > 0){
            invunerableTimer -= dt;
            if (invunerableTimer<=0){
                invunerableTimer = 0;
            }
        }

        if (showHealthBar) {
            speechBubbleTimer -= dt;
            if (speechBubbleTimer < 0) {
                speechBubbleTimer = 0;
            }
        }

        if (attackCooldown > 0){
            attackCooldown -= dt;
            if (attackCooldown <= 0){
                attackCooldown = 0;
            }
        }
        // Keep player on play area
        if (!canWalkOnWall) {
            position.y = MathUtils.clamp(position.y, bottomPlayArea, topPlayArea);
        }
        position.x = MathUtils.clamp(position.x, leftEdge, rightEdge) ;

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

        if (position.y + position.z < -height.floatValue()){
            health = -1;
        }

        if (isAttacking) {
            tex = attackAnimation.getKeyFrame(animationTimer.floatValue());
        }
        else if (isMoving) {
            tex = walkAnimation.getKeyFrame(timer);
        } else {
            tex = walkAnimation.getKeyFrame(0);
        }

        setPosition(position.x + bounceBack.x, position.y + bounceBack.y);
        bounceBack.scl(0.8f);
        if (bounceBack.epsilonEquals(0.0f, 0.0f, 0.1f)) {
            bounceBack.set(0f, 0f);
        }

        // Shadows!
        // Master shadow position
        shadowMasterRectangle.set(position.x, position.y + shadowYOffset, shadowDrawWidth, height.floatValue());
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
        batch.setColor(tintColor.r,tintColor.g,tintColor.b,alpha);
        if (isFacingRight) {
            batch.draw(tex, position.x, position.y + position.z, width, height.floatValue());
        } else {
            batch.draw(tex, position.x + (width/1.5f), position.y + position.z, -width, height.floatValue());
        }
        batch.setColor(Color.WHITE);

        if (showHealthBar && speechBubbleTimer > 0 && speechText != null){
            Assets.emuLogicFont.getData().setScale(.7f);
            float maxSpeechWidth = 150;
            Assets.glyphLayout.setText(Assets.emuLogicFont, speechText, Color.BLACK, maxSpeechWidth, Align.left, true);

            if (position.x > level.screen.cameraCenter.x){
                Assets.speechBubble.draw(batch, position.x,
                        position.y + position.z + height.floatValue() + 10,
                        Assets.glyphLayout.width + 20,
                        Assets.glyphLayout.height + 20);
                Assets.emuLogicFont.draw(batch, Assets.glyphLayout,
                        position.x + 10,
                        position.y + position.z + height.floatValue() + 20 + Assets.glyphLayout.height);
            } else {
                Assets.speechBubble.draw(batch, position.x - Assets.glyphLayout.width - 20,
                                         position.y + position.z + height.floatValue() + 10,
                                         Assets.glyphLayout.width + 20,
                                         Assets.glyphLayout.height + 20);
                Assets.emuLogicFont.draw(batch, Assets.glyphLayout,
                                         position.x - Assets.glyphLayout.width - 10,
                                         position.y + position.z + height.floatValue() + 20 + Assets.glyphLayout.height);
            }

        }

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
        // Fuck it, its ludum dare
        if (this instanceof Player) {
            Sounds.play(Sounds.Effect.playerJump);
        }
        jumpCount++;
        verticalVelocity = jumpVelocity;
        position.z += .01f;
    }

    public void attack(){
        if (attackCooldown > 0 || isInvulerable()) return;    // Early out if you are attacking

        attackCooldown = ATTACK_COOLDOWN;
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
        return invunerableTimer > 0 || dead;
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
        if (dead) return;
        if ((health -= dmg) <= 0) {
            health = 0;
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
            if (floating || !notSafeToWalk(testPosition)) {
                setPosition(movePoint.x, movePoint.y);
            }
            moveLeft -= dist;
            movePoint.setZero();
        } else {
            direction.set(movePoint.cpy().sub(position.x, position.y));
            direction.nor().scl(moveLeft);
            testPosition.set(position);
            testPosition.add(direction.x, direction.y, 0);
            if (!floating && notSafeToWalk(testPosition)) {
                movePoint.setZero();
            } else {
                if (!setPosition(testPosition)){
                    movePoint.setZero();
                }
            }
            moveLeft = 0;
        }
        return moveLeft;
    }

    public boolean setPosition(Vector3 newPos){
        return setPosition(newPos.x, newPos.y, newPos.z);
    }

    public boolean setPosition(float x, float y){
        return setPosition(x, y, position.z);
    }
    public boolean setPosition(float x, float y, float z){
        float leftY = level.getTopBound(x);
        float rightY = level.getTopBound(x+characterSpriteWidth);
        if (floating  || (y < leftY && y < rightY)){
            position.set(x, y, z);
            return true;
        }
        return false;
    }

    public void say(String text){
        speechText = text;
        speechBubbleTimer = 3f;
    }
}
