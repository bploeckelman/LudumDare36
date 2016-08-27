package lando.systems.ld36.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld36.utils.Assets;

/**
 * Created by dsgraham on 8/27/16.
 */
public class GameObject {

    public static boolean DRAW_BOUNDS = true;

    public final float GRAVITY = -200;
    public float jumpVelocity = 200;

    public Vector3 position;
    public Rectangle hitBounds;
    public float verticalVelocity;
    public TextureRegion tex;
    public TextureRegion shadowTex;
    public boolean isFacingRight = true;
    public float width;
    public float height;

    public GameObject(){
        tex = new TextureRegion(Assets.debugTexture, 50, 50);
        shadowTex = new TextureRegion(Assets.shadowTexture, 32, 32);
        position = new Vector3();
        width = 50;
        height = width;
        hitBounds = new Rectangle(position.x, position.y, tex.getRegionWidth(), tex.getRegionHeight());
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
        batch.draw(shadowTex, position.x, position.y - 10, 64, height);
        if (isFacingRight) {
            batch.draw(tex, position.x, position.y + position.z, width, height);
        } else {
            batch.draw(tex, position.x + (width/1.5f), position.y + position.z, -width, height);
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
        verticalVelocity = jumpVelocity;
        position.z += .01f;
    }
}
