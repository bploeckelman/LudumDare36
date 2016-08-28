package lando.systems.ld36.entities;

import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class SmartPhone extends Enemy {
    public SmartPhone(Level level) {
        super(level);
        walkAnimation = Assets.smartPhone;
        attackAnimation = Assets.smartPhoneKnife;

        tex = walkAnimation.getKeyFrame(timer);

        width = tex.getRegionWidth();
        height = tex.getRegionHeight();

        jumpVelocity = 50f;
        hitBounds = new Rectangle(position.x, position.y, 32f, tex.getRegionHeight());
    }

    public void update(float dt) {
        super.update(dt);
        jump();
        timer += dt;
        hitBounds.x = position.x;
        hitBounds.y = position.y + position.z;
    }
}
