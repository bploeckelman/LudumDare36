package lando.systems.ld36.entities;

import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class SmartPhoneHard extends SmartPhoneMedium {
    public SmartPhoneHard(Level level, float x, float y) {
        super(level, x, y);

        walkAnimation = Assets.smartPhoneHard;
        attackAnimation = Assets.smartPhoneKnifeHard;

        tex = walkAnimation.getKeyFrame(timer);
    }
}
