package lando.systems.ld36.entities;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.KeyMapping;

/**
 * Created by dsgraham on 8/27/16.
 */
public class Player extends GameObject {

    private static final float HIT_DELTA_X = 32;
    private static final float HIT_DELTA_Y = 32;
    private static final float HIT_DELTA_Z = 32;


    public int attackPower = 1;

    public Rectangle footBounds;


    public int health = 100;
    public int deaths = 0;

    public Player(Level level) {
        this(PlayerCharacter.FLOPPY, level);
    }

    public Player(PlayerCharacter character, Level level){
        super(level);
        this.moveSpeed = character.moveSpeed;
        this.attackPower = character.attackPower;

        animationTimer = new MutableFloat(0f);
        this.walkAnimation = character.walkAnimation;
        tex = walkAnimation.getKeyFrame(timer);

        this.attackAnimation = character.attackAnimation;

        width = tex.getRegionWidth();
        height = tex.getRegionHeight();

        isFacingRight = true;
        footBounds = new Rectangle();
        hitBounds = new Rectangle(position.x + 15f, position.y + 4f, 30f, tex.getRegionHeight() - 8f);
    }

    public void update(float dt, float leftEdge){
        super.update(dt);
        if (dead){
            level.screen.screenShake.shake(1f);
            respawn();
        }

        isMoving = false;
        this.leftEdge = leftEdge;

        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.JUMP)){
            jump();
        }

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


        if(Assets.keyMapping.isActionPressed(KeyMapping.ACTION.ATTACK)) {
            attack();
        }



        hitBounds.x = position.x + 15f;
        hitBounds.y = position.y + position.z;
    }

    public void render(SpriteBatch batch){
        super.render(batch);
    }

    public int doesHit(Enemy enemy) {
        if (enemy.position.y > (this.position.y - HIT_DELTA_Y)
         && enemy.position.y < (this.position.y + HIT_DELTA_Y)
         && enemy.position.z > (this.position.z - HIT_DELTA_Z)
         && enemy.position.z < (this.position.z + HIT_DELTA_Z)) {
            if (isFacingRight && (enemy.hitBounds.x > hitBounds.x) && (enemy.hitBounds.x < hitBounds.x + hitBounds.width + HIT_DELTA_X)) {
                return 1;
            }
            if (!isFacingRight && (enemy.hitBounds.x + enemy.hitBounds.width < hitBounds.x + hitBounds.width)
                               && (enemy.hitBounds.x + enemy.hitBounds.width > hitBounds.x - HIT_DELTA_X)) {
                return -1;
            }
        }
        return 0;
    }

    public void respawn(){
        dead = false;
        deaths++;
        position.z = 0;
        jumpCount =0;
        verticalVelocity = 0;
        position.x = lastSafePlace.get(0).x;
        position.y = lastSafePlace.get(0).y;
        invunerableTimer = INVULERABLITIYDELAY;
    }
}
