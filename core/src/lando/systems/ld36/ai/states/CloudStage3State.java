package lando.systems.ld36.ai.states;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld36.entities.Cloud;
import lando.systems.ld36.entities.CloudProjectile;
import lando.systems.ld36.entities.GameObject;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.accessors.Vector2Accessor;
import lando.systems.ld36.utils.accessors.Vector3Accessor;

/**
 * Created by dsgraham on 8/29/16.
 */
public class CloudStage3State extends State {
    public Cloud cloud;
    public float delay;
    public final float SWOOPING_DELAY = 4f;
    OrthographicCamera cam;
    GameObject player;
    int direction;

    public CloudStage3State(GameObject owner) {
        super(owner);
        cloud = (Cloud) owner;
        cam = cloud.level.screen.camera;
        player = cloud.level.player;
        direction = 0;
    }

    @Override
    public void update(float dt) {
        float oldDelay = delay;
        delay -= dt;

        if (delay > 2.5f && delay < 2.8f && delay % .1f > .05f) {
            cloud.tintColor = Color.RED;
        } else {
            cloud.tintColor = Color.WHITE;
        }

        if (oldDelay > 2 && delay <= 2){
            int randomRot = MathUtils.random(180);
            for (int i = 0; i < 12; i++){
                Vector2 pos = new Vector2(cloud.position.x + 64, cloud.position.y + cloud.position.z + 15);
                Vector2 vel = new Vector2(MathUtils.sinDeg(i * (360/12) + randomRot), MathUtils.cosDeg(i * (360/12)  + randomRot));
                vel.scl(200);
                cloud.projectiles.add(new CloudProjectile(pos, vel, 15));
            }
        }
        if (oldDelay > 1 && delay <= 1){
            Tween.to(cloud.position, Vector3Accessor.XYZ, .8f)
                    .waypoint(cloud.position.x + 10, cloud.position.y, cloud.position.z - 10)
                    .waypoint(cloud.position.x, cloud.position.y, cloud.position.z - 20)
                    .waypoint(cloud.position.x - 10, cloud.position.y, cloud.position.z - 10)
                    .target(cloud.position.x, cloud.position.y, cloud.position.z)
                    .ease(Sine.INOUT)
                    .start(Assets.tween);
        }

        if (delay < 0){
            Vector3 side = getLeftSidePos();
            direction = -1;
            if (cloud.position.x < cam.position.x){
                side = getRightSidePos();
                direction = 1;
            }

            Tween.to(cloud.position, Vector3Accessor.XYZ, 1f)
                    .waypoint(player.position.x, player.position.y, player.position.z)
                    .target(side.x, side.y, side.z)
                    .ease(Sine.INOUT)
                    .start(Assets.tween);

            delay += SWOOPING_DELAY;
        }

        if (!cloud.isInvulerable() && !player.isInvulerable() && cloud.hitBounds.overlaps(player.hitBounds)){
            Assets.particles.addBloodParticles(player.hitBounds, direction, 11);
            player.getHurt(11, direction);
            player.invunerableTimer = 1f;
        }

    }

    @Override
    public void onEnter() {
        delay = SWOOPING_DELAY + 1f;
        Vector3 leftSide = getLeftSidePos();
        Tween.to(cloud.position, Vector3Accessor.XYZ, 1f)
                .target(leftSide.x, leftSide.y, leftSide.z)
                .start(Assets.tween);
        cloud.invunerableTimer = 1f;
        owner.say("Dodge this!");
    }

    @Override
    public void onExit() {

    }

    private Vector3 getLeftSidePos(){
        return new Vector3(cam.position.x - cam.viewportWidth/2 + 32, cam.position.y - cam.viewportHeight/2 + 120, 150);
    }

    private Vector3 getRightSidePos(){
        return new Vector3(cam.position.x + cam.viewportWidth/2 - 160, cam.position.y - cam.viewportHeight/2 + 120, 150);
    }
}
