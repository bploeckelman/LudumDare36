package lando.systems.ld36.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld36.utils.Assets;

/**
 * Created by dsgraham on 8/27/16.
 */
public class GameObject {
    public final float GRAVITY = -350;
    public float jumpVelocity = 200;

    public Vector3 position;
    public float verticalVelocity;
    public TextureRegion tex;
    public TextureRegion shadowTex;
    public boolean facingLeft;
    public float width;

    public GameObject(){
        tex = new TextureRegion(Assets.debugTexture, 50, 50);
        shadowTex = new TextureRegion(Assets.shadowTexture, 32, 32);
        position = new Vector3();
        width = 50;
    }

    public void update(float dt){
        // Jumping
        if (isOnGround()){
            verticalVelocity = 0;
        } else {
            verticalVelocity += GRAVITY * dt;
        }
        position.z += verticalVelocity * dt;



    }

    public void render(SpriteBatch batch){
        batch.draw(shadowTex, position.x, position.y, width, width);
        batch.draw(tex, position.x, position.y + position.z, width, width);
    }


    public boolean isOnGround(){
        if (position.z < 0){
            position.z = 0;
        }
        return position.z <= 0;
    }

    public void jump(){
        verticalVelocity = jumpVelocity;
        position.z += .01f;
    }
}
