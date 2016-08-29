package lando.systems.ld36.ai.states;

import aurelienribon.tweenengine.Tween;
import lando.systems.ld36.entities.GameObject;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Sounds;

/**
 * Created by dsgraham on 8/29/16.
 */
public class GrowState extends State {
    public float height;

    public GrowState(GameObject owner, float height) {
        super(owner);
        this.height = height;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void onEnter() {
        Tween.to(owner.height, -1, 2f)
                .target(height)
                .start(Assets.tween);
        Sounds.play(Sounds.Effect.cloudSpawn1);
    }

    @Override
    public void onExit() {
        owner.showHealthBar = true;
        Sounds.play(Sounds.Effect.cloudSpawn2);
    }
}
