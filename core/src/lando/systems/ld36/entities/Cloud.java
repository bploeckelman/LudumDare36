package lando.systems.ld36.entities;

import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class Cloud extends Enemy {
    public Cloud(Level level, float x, float y) {
        super(level);

        position.x = x;
        position.y = y;

        walkAnimation = Assets.cloud;
        attackAnimation = Assets.cloud;

        tex = walkAnimation.getKeyFrame(timer);
        hitBounds = new Rectangle(position.x, position.y, 128f, tex.getRegionHeight());


        width = 128;

        isMoving = true;
        characterSpriteWidth = 128;
        shadowDrawWidth = 128;
        shadowYOffset = -15;
    }

    public void update(float dt) {
        super.update(dt);
        isFacingRight = true;
        verticalVelocity = 0; // cloud floats
    }
}
