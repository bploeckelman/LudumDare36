package lando.systems.ld36.entities;

import com.badlogic.gdx.graphics.Color;
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
    public final float INVULERABLITIYFLASHSPEED = .5f;
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

    }

    public void update(float dt){

        if (invunerableTimer > 0){
            invunerableTimer -= dt;
            if (invunerableTimer<=0){
                invunerableTimer = 0;
            }
        }
        // Keep player on play area
        position.y = MathUtils.clamp(position.y, bottomPlayArea, topPlayArea);
        position.x = MathUtils.clamp(position.x, leftEdge, level.getLevelWidth() - (width/2)) ;

        level.getGroundTiles(position, tiles);
        footBounds.set(position.x + 10, position.y, 45, 5);
        falling = true;
        for (Rectangle tile : tiles){
            if (position.z >= 0 && footBounds.overlaps(tile) || tile.contains(footBounds)){
                falling = false;
                break;
            }
        }



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
    }

    public void render(SpriteBatch batch){
        if (!falling) {
            batch.draw(shadowTex, position.x, position.y - 10, 64, height);
        }

        // Flash the thing if it is invulnerable
        float alpha = 1;
        if (invunerableTimer > 0){
            alpha = (invunerableTimer % INVULERABLITIYFLASHSPEED) * (1/INVULERABLITIYFLASHSPEED);
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

    public boolean isInvulerable(){
        return invunerableTimer > 0;
    }
}
