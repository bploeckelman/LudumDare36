package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class SmartPhoneEasy extends Enemy {
    public SmartPhoneEasy(Level level, float x, float y, boolean isBoss) {
        super(level);
        walkAnimation = Assets.smartPhone;
        attackAnimation = Assets.smartPhoneKnife;

        tex = walkAnimation.getKeyFrame(timer);

        width = tex.getRegionWidth();
        height = new MutableFloat(tex.getRegionHeight());

        position.x = x;
        position.y = y;

        jumpVelocity = 50f;
        hitBounds = new Rectangle(position.x, position.y, 32f, tex.getRegionHeight());
        attackPower = 8;
        moveSpeed = 75;

        if (isBoss) level.boss = this;
        name = "Shutters";
        taunt = "Shutters phones are great for business";
    }

    public void update(float dt) {
        super.update(dt);
        jump();
        timer += dt;

    }
}
