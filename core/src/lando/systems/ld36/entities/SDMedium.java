package lando.systems.ld36.entities;

import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class SDMedium extends SDEasy {
    public SDMedium(Level level, float x, float y) {
        super(level, x, y);

        walkAnimation = Assets.sdWalkMedium;
        attackAnimation = Assets.sdAttackMedium;

        tex = walkAnimation.getKeyFrame(timer);

        attackPower = 20;
    }
}
