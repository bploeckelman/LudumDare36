package lando.systems.ld36.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

/**
 * Created by dsgraham on 8/27/16.
 */
public class GameObject {

    public static boolean DRAW_BOUNDS = true;

    public final float GRAVITY = -200;
    public float jumpVelocity = 200;

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

    }

    public void update(float dt){
        // Jumping
        if (isOnGround()){
            verticalVelocity = 0;
        } else {
            verticalVelocity += GRAVITY * dt;
        }
        position.z += verticalVelocity * dt;

        // Keep player on play area
        position.y = MathUtils.clamp(position.y, bottomPlayArea, topPlayArea);
        position.x = MathUtils.clamp(position.x, leftEdge, level.getLevelWidth() - (width/2)) ;

        level.getGroundTiles(position, tiles);
        footBounds.set(position.x + 10, position.y, 45, 5);
        falling = true;
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
