package lando.systems.ld36.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld36.utils.Assets;

/**
 * Created by dsgraham on 8/29/16.
 */
public class CloudProjectile {
    Vector2 position;
    Vector2 velocity;
    Rectangle hitBox;
    int damage;
    float timeAccum;

    public CloudProjectile(Vector2 position, Vector2 velocity, int damage){
        this.position = position;
        this.velocity = velocity;
        this.damage = damage;
        hitBox = new Rectangle(position.x, position.y, 20, 20);
        timeAccum = 0;
    }

    public void update(float dt){
        timeAccum += dt;
        position.x += velocity.x * dt;
        position.y += velocity.y * dt;
        hitBox.x = position.x;
        hitBox.y = position.y;
    }

    public void render(SpriteBatch batch){
        //TODO make them awesome
        batch.draw(Assets.energy_ball.getKeyFrame(timeAccum), hitBox.x-10, hitBox.y-10, hitBox.width+20, hitBox.height+20);
    }
}
