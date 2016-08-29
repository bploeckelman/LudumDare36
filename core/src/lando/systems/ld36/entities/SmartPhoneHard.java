package lando.systems.ld36.entities;

import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;

public class SmartPhoneHard extends SmartPhoneMedium {
    public SmartPhoneHard(Level level, float x, float y, boolean isBoss) {
        super(level, x, y, isBoss);

        walkAnimation = Assets.smartPhoneHard;
        attackAnimation = Assets.smartPhoneKnifeHard;

        tex = walkAnimation.getKeyFrame(timer);
        attackPower = 15;
        moveSpeed = 95;
    }
}
