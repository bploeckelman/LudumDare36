package lando.systems.ld36.ai.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld36.entities.Cloud;
import lando.systems.ld36.entities.CloudProjectile;
import lando.systems.ld36.entities.GameObject;

/**
 * Created by dsgraham on 8/29/16.
 */
public class CloudStage1State extends State {
    public Cloud cloud;
    public final float SHOOT_DELAY = 3f;
    public float projRotation;

    public CloudStage1State(GameObject owner) {
        super(owner);
        cloud = (Cloud) owner;
        projRotation = 0;
    }

    @Override
    public void update(float dt) {
        float moveLeft = owner.moveSpeed * dt;
        while (moveLeft > 0) {
            if (owner.movePoint.epsilonEquals(0, 0, .5f)) {
                float x = owner.position.x + MathUtils.random(500) - 250;
                float y = owner.position.y + MathUtils.random(200) - 100;
                y = MathUtils.clamp(y, owner.bottomPlayArea, owner.topPlayArea);
                x = MathUtils.clamp(x, owner.leftEdge, owner.level.getLevelWidth() - (owner.width / 2));
                owner.movePoint.set(x, y);
            }
            owner.movePoint.x = MathUtils.clamp(owner.movePoint.x,
                    owner.level.screen.cameraCenter.x - owner.level.screen.camera.viewportWidth/2,
                    owner.level.screen.cameraCenter.x + owner.level.screen.camera.viewportWidth/2 - owner.width);
            moveLeft = owner.updateMove(dt, moveLeft);
        }

        cloud.shootDelay -= dt;
        if (cloud.shootDelay <= 0){
            cloud.shootDelay += SHOOT_DELAY;
            for (int i = 0; i < 4; i++){
                Vector2 pos = new Vector2(cloud.position.x + 64, cloud.position.y + cloud.position.z + 15);
                Vector2 vel = new Vector2(MathUtils.sinDeg(i * 90 + projRotation), MathUtils.cosDeg(i * 90 + projRotation));
                vel.scl(200);
                cloud.projectiles.add(new CloudProjectile(pos, vel, 5));
            }
            projRotation += 45;
        }
        if (cloud.shootDelay > .5f && cloud.shootDelay < .8f && cloud.shootDelay % .1f > .05f) {
            cloud.tintColor = Color.RED;
        } else {
            cloud.tintColor = Color.WHITE;
        }
    }

    @Override
    public void onEnter() {
        cloud.shootDelay = SHOOT_DELAY;
        cloud.moveSpeed = 100;
        owner.say("Death from above!");
    }

    @Override
    public void onExit() {
    }
}
