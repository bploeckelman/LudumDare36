package lando.systems.ld36.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld36.utils.Assets;

public enum PlayerCharacter {
    FLOPPY("Floppy D", Assets.floppyWalk, Assets.floppyPunch, 180),
    BETAMAX("Beta Max", Assets.betamaxWalk, Assets.betamaxPunch, 250),
    EIGHTTRACK("Eight Trucker", Assets.eightTrackWalk, Assets.eightTrackPunch, 140);

    public static float maxMoveSpeed = 250;

    public String name;
    public Animation walkAnimation;
    public Animation attackAnimation;
    public TextureRegion keyframe;
    public float moveSpeed;

    // TODO: Add attack power?

    PlayerCharacter(String name, Animation walkAnimation, Animation attackAnimation, float moveSpeed) {
        this.name = name;
        this.walkAnimation = walkAnimation;
        this.attackAnimation = attackAnimation;
        this.moveSpeed = moveSpeed;
        this.keyframe = walkAnimation.getKeyFrame(0f);
    }
}
