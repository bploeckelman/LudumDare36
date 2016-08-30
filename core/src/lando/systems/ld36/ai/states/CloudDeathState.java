package lando.systems.ld36.ai.states;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lando.systems.ld36.entities.Cassette;
import lando.systems.ld36.entities.GameObject;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Sounds;
import lando.systems.ld36.utils.accessors.Vector3Accessor;

/**
 * Created by dsgraham on 8/29/16.
 */
public class CloudDeathState extends State {
    public CloudDeathState(GameObject owner) {
        super(owner);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void onEnter() {
        owner.showHealthBar = false;
        owner.level.allowPlayerInput = false;
        owner.invunerableTimer = 2f;

        OrthographicCamera cam = owner.level.screen.camera;
        final Player p = owner.level.player;
        final Cassette cas = new Cassette(owner.level, cam.position.x + cam.viewportWidth /2, 100);
        cas.isFacingRight = false;
        owner.level.objects.add(cas);

        Tween.to(owner.height, -1, 2f)
                .target(0)
                .start(Assets.tween);
        Timeline.createSequence()
                .pushPause(2f)
                .beginParallel()
                    .push(Tween.to(p.position, Vector3Accessor.XY, 3f)
                            .target(cam.position.x - p.characterSpriteWidth, 100)
                            .setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int i, BaseTween<?> baseTween) {
                                    p.isFacingRight = true;
                                }
                            }))
                    .push(Tween.to(cas.position, Vector3Accessor.XY, 3f)
                            .target(cam.position.x + 20, 100)
                            .delay(2f))
                .end()
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        cas.say("You saved me!!!");
                    }
                }))
                .pushPause(1f)
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        p.say("I Know");
                        owner.dead = true;
                    }
                }))
                .start(Assets.tween);
        Sounds.play(Sounds.Effect.bossDead);
        Sounds.play(Sounds.Effect.cloudDead);
    }

    @Override
    public void onExit() {

    }
}
