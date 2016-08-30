package lando.systems.ld36.entities;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.KeyMapping;
import lando.systems.ld36.utils.Sounds;
import lando.systems.ld36.utils.Statistics;

/**
 * Created by dsgraham on 8/27/16.
 */
public class Player extends GameObject {


    public Rectangle footBounds;


    public Player(Level level) {
        this(PlayerCharacter.FLOPPY, level);
    }

    public Player(PlayerCharacter character, Level level){
        super(level);
        this.moveSpeed = character.moveSpeed;
        this.attackPower = character.attackPower;

        this.walkAnimation = character.walkAnimation;
        tex = walkAnimation.getKeyFrame(timer);

        this.attackAnimation = character.attackAnimation;

        width = tex.getRegionWidth();
        height = new MutableFloat(tex.getRegionHeight());

        isFacingRight = true;
        footBounds = new Rectangle();
        hitBounds = new Rectangle(position.x + 15f, position.y + 4f, 30f, tex.getRegionHeight() - 8f);
        health = 100;
        maxHealth = 100;
        attack_range = 32;
    }

    public void update(float dt, float leftEdge, float rightEdge){
        super.update(dt);
        if (health <= 0 && !dead) {
            Sounds.play(Sounds.Effect.playerDeath);
            dead = true;
        }
        if (dead){
            if (respawnTimer > 1) {
                level.screen.screenShake.shake(1f);
            }
            respawnTimer -= dt;
            if (respawnTimer < 0) {
                respawn();
            }
        }

        isMoving = false;
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;

        if (level.allowPlayerInput) {
            if (Assets.keyMapping.isActionJustPressed(KeyMapping.ACTION.JUMP)) {
                jump();
            }

//            //TODO REMOVE ME
//            if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
//                for(int i = 0; i < level.objects.size; i++)
//                {
//                    GameObject obj = level.objects.get(i);
//                    if (obj instanceof Enemy) obj.health = 0;
//                }
//                level.boss.health = 0;
//            }

            if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.RIGHT) &&
                    Assets.keyMapping.isActionPressed(KeyMapping.ACTION.LEFT)) {
                // Do nothing
            } else if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                setPosition(position.x + moveSpeed * dt * (falling ? 0.5f : 1f), position.y);
                isMoving = true;
                isFacingRight = true;
            } else if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.LEFT) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                setPosition(position.x - moveSpeed * dt * (falling ? 0.5f : 1f), position.y);
                isMoving = true;
                isFacingRight = false;
            }

            if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.UP) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                setPosition(position.x, position.y + moveSpeed * dt);
                isMoving = true;
            }
            if (Assets.keyMapping.isActionPressed(KeyMapping.ACTION.DOWN) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                setPosition(position.x, position.y - moveSpeed * dt);
                isMoving = true;
            }


            if (Assets.keyMapping.isActionJustPressed(KeyMapping.ACTION.ATTACK)) {
                attack();
            }

        }



        hitBounds.x = position.x + 15f;
        hitBounds.y = position.y + position.z;
    }

    public void render(SpriteBatch batch){
        super.render(batch);
    }



    public void respawn(){
        dead = false;
        Statistics.deaths++;
        position.z = 0;
        jumpCount =0;
        verticalVelocity = 0;
        health = maxHealth;
        position.x = lastSafePlace.get(0).x;
        position.y = lastSafePlace.get(0).y;
        invulerabilityFlashSpeed = .5f;
        invunerableTimer = RESPAWNDELAY;
    }

    public void attack() {
        if (isAttacking || isInvulerable()) return;
        Statistics.punchesThrown++;
        super.attack();
    }

    public void getHurt(int dmg, int dir) {
        Statistics.damageTaken += dmg;
        super.getHurt(dmg, dir);
        Sounds.play(Sounds.Effect.playerHurt);
    }
}
