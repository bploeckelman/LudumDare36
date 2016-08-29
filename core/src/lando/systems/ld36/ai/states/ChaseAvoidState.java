package lando.systems.ld36.ai.states;

import lando.systems.ld36.entities.GameObject;

/**
 *
 */
public class ChaseAvoidState extends State {
    public ChaseAvoidState(GameObject owner) {
        super(owner);
    }

    @Override
    public void update(float dt) {
        float moveLeft = owner.moveSpeed * dt;
        float offsetX = owner.position.x < owner.level.player.position.x ? -32 : 32;
        float offsetY = 0;
        for (int i = 0; i < owner.level.objects.size; i++){
            GameObject obj = owner.level.objects.get(i);
            if (obj == owner || obj == owner.level.player) continue;

            float difX = owner.position.x - obj.position.x;
            if (difX < .1f && difX > -.1f) difX = .1f;

            float difY = owner.position.y - obj.position.y;
            if (difY < .1f && difY > -.1f) difY = .1f;

            offsetX += 128 / difX;
            offsetY += 128 / difY;
        }
        owner.movePoint.set(owner.level.player.position.x + offsetX, owner.level.player.position.y + offsetY);
        moveLeft = owner.updateMove(dt, moveLeft);
        owner.direction.set(owner.level.player.position.x - owner.position.x, owner.level.player.position.y - owner.position.y);

    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }
}
