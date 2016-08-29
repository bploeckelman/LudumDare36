package lando.systems.ld36.entities;

import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class SDHard extends SDMedium {
    public SDHard(Level level, float x, float y) {
        super(level, x, y);

        walkAnimation = Assets.sdWalkHard;
        attackAnimation = Assets.sdAttackHard;

        tex = walkAnimation.getKeyFrame(timer);

        attackPower = 30;
    }
}
