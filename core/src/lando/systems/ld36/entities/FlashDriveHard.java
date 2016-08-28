package lando.systems.ld36.entities;

import lando.systems.ld36.levels.Level;

public class FlashDriveHard extends FlashDriveMedium {
    public FlashDriveHard(Level level, float x, float y) {
        super(level, x, y);

        health = 10;
    }
}
