package lando.systems.ld36.entities;

import lando.systems.ld36.utils.Assets;

public class EightTrack extends Enemy {
    public EightTrack() {
        super();
        walkAnimation = Assets.eightTrackWalk;
        attackAnimation = Assets.eightTrackPunch;

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