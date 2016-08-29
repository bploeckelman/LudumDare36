package lando.systems.ld36.entities;

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

        width = 128;

        isMoving = true;
    }

    public void update(float dt) {
        super.update(dt);
        isFacingRight = true;
    }
}
