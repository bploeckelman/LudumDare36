package lando.systems.ld36.entities;

import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class SmartPhoneMedium extends SmartPhoneEasy {
    public SmartPhoneMedium(Level level, float x, float y) {
        super(level, x, y);

        walkAnimation = Assets.smartPhoneMedium;
        attackAnimation = Assets.smartPhoneKnifeMedium;

        tex = walkAnimation.getKeyFrame(timer);
        attackPower = 12;
    }
}
