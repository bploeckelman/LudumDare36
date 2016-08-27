package lando.systems.ld36.entities;

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
    public float timer = 0f;
    public Animation walkAnimation;

    public Player(){
        walkAnimation = Assets.floppyWalk;
        tex = walkAnimation.getKeyFrame(timer);
    }

    public void update(float dt){
        super.update(dt);

        timer += dt;
        isMoving = false;

        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.JUMP) && isOnGround()){
            jump();
        }

        // TODO: Don't allow movement off of the screen;
        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.RIGHT)){
            position.x += moveSpeed * dt;
            isMoving = true;
        }
        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.LEFT)){
            position.x -= moveSpeed * dt;
            isMoving = true;
        }
        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.UP)){
            position.y += moveSpeed * dt;
            isMoving = true;
        }
        if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.DOWN)){
            position.y -= moveSpeed * dt;
            isMoving = true;
        }

        if (isMoving) {
            tex = walkAnimation.getKeyFrame(timer);
        }
    }
}
