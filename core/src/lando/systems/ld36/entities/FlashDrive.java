package lando.systems.ld36.entities;

import lando.systems.ld36.utils.Assets;

public class FlashDrive extends Enemy {
    public FlashDrive() {
        super();
        walkAnimation = Assets.flashWalk;
        attackAnimation = Assets.flashPunch;

        tex = walkAnimation.getKeyFrame(timer);

        width = tex.getRegionWidth();
        height = tex.getRegionHeight();

        jumpVelocity = .05f;
    }

    public void update(float dt) {
        timer += dt;

        super.update(dt);
    }
}
