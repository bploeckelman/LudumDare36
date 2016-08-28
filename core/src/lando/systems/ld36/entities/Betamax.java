package lando.systems.ld36.entities;

import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class Betamax extends Enemy {
    public Betamax(Level level) {
        super(level);
        walkAnimation = Assets.betamaxWalk;
        attackAnimation = Assets.betamaxPunch;

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
