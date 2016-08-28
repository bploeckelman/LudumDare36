package lando.systems.ld36.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.KeyMapping;

/**
 * Created by dsgraham on 8/27/16.
 */
public class Player extends GameObject {


    public Rectangle footBounds;


    public int deaths = 0;

    public Player(Level level) {
        this(PlayerCharacter.FLOPPY, level);
    }

    public Player(PlayerCharacter character, Level level){
        super(level);
        this.moveSpeed = character.moveSpeed;
        this.attackPower = character.attackPower;

        this.walkAnimation = character.walkAnimation;
        tex = walkAnimation.getKeyFrame(timer);

        this.attackAnimation = character.attackAnimation;

        width = tex.getRegionWidth();
        height = tex.getRegionHeight();

        isFacingRight = true;
        footBounds = new Rectangle();
        hitBounds = new Rectangle(position.x + 15f, position.y + 4f, 30f, tex.getRegionHeight() - 8f);
        health = 100;
        attack_range = 32;
    }

    public void update(float dt, float leftEdge){
        super.update(dt);
        if (dead){
            if (respawnTimer > 1) {
                level.screen.screenShake.shake(1f);
            }
            respawnTimer -= dt;
            if (respawnTimer < 0) {
                respawn();
            }
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


        if(Assets.keyMapping.isActionJustPressed(KeyMapping.ACTION.ATTACK)) {
            attack();
        }



        hitBounds.x = position.x + 15f;
        hitBounds.y = position.y + position.z;
    }

    public void render(SpriteBatch batch){
        super.render(batch);
    }



    public void respawn(){
        dead = false;
        deaths++;
        position.z = 0;
        jumpCount =0;
        verticalVelocity = 0;
        health = 100;
        position.x = lastSafePlace.get(0).x;
        position.y = lastSafePlace.get(0).y;
        invulerabilityFlashSpeed = .5f;
        invunerableTimer = RESPAWNDELAY;
    }
}
