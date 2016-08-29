package lando.systems.ld36.ai.conditions;

import lando.systems.ld36.entities.GameObject;
import lando.systems.ld36.utils.Sounds;

/**
 * Created by dsgraham on 8/29/16.
 */
public class BossDeadCondition extends Condition {
    public BossDeadCondition(GameObject owner) {
        super(owner);
    }

    @Override
    public boolean isTrue() {
        if (owner.level.boss.dead) {
            Sounds.play(Sounds.Effect.bossDead);
        }
        return owner.level.boss.dead;
    }
}
