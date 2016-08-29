package lando.systems.ld36.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.entities.Boundary;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.entities.PlayerCharacter;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.*;

/**
 * Created by Brian on 8/27/2016.
 */
public class GameScreen extends BaseScreen {

    final float NAG_DELAY = 5f;
    Player debugPlayer;
    Level level;
    float hudHeight = 100;
    float hudBorderWidth = 4;
    public Shake screenShake;
    public Vector2 cameraCenter;
    public float lastCameraX;
    public float lagdelay = 0;
    public Color healthColor;
    public MutableFloat nagSize;
    public float cameraDelay;

    public GameScreen(PlayerCharacter character) {
        camera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        camera.update();
        screenShake = new Shake(35, 8);
        level = new Level(Script.getLevelFileName(), this);
        debugPlayer = new Player(character, level);
        level.setPlayer(debugPlayer);
        level.initilizeStates();
        cameraCenter = new Vector2(camera.position.x, camera.position.y);
        nagSize = new MutableFloat(0);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare36.game.setScreen(new CharacterSelectScreen());
        }

        cameraDelay -= dt;
        if (cameraDelay < 0) cameraDelay = 0;

        debugPlayer.update(dt, cameraCenter.x - camera.viewportWidth/2, cameraCenter.x + camera.viewportWidth/2 - 64);

        Boundary boundary = level.getActiveBoundry();



        float screenYDif = (debugPlayer.position.y - cameraCenter.y + 50) * .1f;
        if (MathUtils.isEqual(screenYDif, 0, .5f)){
            screenYDif = 0;
        }
        cameraCenter.y += screenYDif;


        cameraCenter.y = Math.max(cameraCenter.y, camera.viewportHeight/2);

        if (boundary == null && cameraDelay <= 0) {
            // have camera follow player
            if (debugPlayer.position.x > cameraCenter.x) {
                float screenXDif = (debugPlayer.position.x - cameraCenter.x) * .05f;
                if (MathUtils.isEqual(screenXDif, 0, .5f)){
                    screenXDif = 0;
                }
                cameraCenter.x += screenXDif;
            }

            boolean atRightEdge = false;
            if (cameraCenter.x >= level.getLevelWidth() - (camera.viewportWidth / 2)) {
                atRightEdge = true;
                cameraCenter.x = level.getLevelWidth() - (camera.viewportWidth / 2);
            }

            if (cameraCenter.x == lastCameraX &&
               (level.activeBoundries() > 0 || !atRightEdge) && level.getActiveEnemies() == 0){
                lagdelay += dt;
            } else {
                lagdelay = 0;
                if (nagSize.floatValue() == 1)
                Tween.to(nagSize, -1, 1f)
                        .target(.25f)
                        .ease(Back.IN)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                nagSize.setValue(0);
                            }
                        })
                        .start(Assets.tween);
            }


            lastCameraX = cameraCenter.x;
        }

        if (lagdelay > NAG_DELAY){
            if (nagSize.floatValue() == 0){
                nagSize.setValue(.5f);
                Tween.to(nagSize, -1, 1f)
                        .target(1f)
                        .ease(Elastic.OUT)
                        .start(Assets.tween);

            }
        }

//        camera.update();
        screenShake.update(dt, camera, cameraCenter);

        level.update(dt);

        if (level.completed && cameraDelay <= 0){
            Statistics.currentLevel++;
            LudumDare36.game.setScreen(new TextScreen(Script.getScript(Statistics.currentLevel), new CharacterSelectScreen()));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.shapes.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        level.render(batch, camera);
        Assets.particles.render(batch);
        batch.end();

        batch.begin();
        batch.setProjectionMatrix(hudCamera.combined);
        // Draw HUD stuff

        TextureRegion keepGoing = Assets.keepGoing.getKeyFrame(lagdelay);
        batch.draw(keepGoing,
                hudCamera.viewportWidth - ((keepGoing.getRegionWidth()*nagSize.floatValue()) + 20),
                200,
                keepGoing.getRegionWidth() * nagSize.floatValue(),
                keepGoing.getRegionHeight() * nagSize.floatValue());


        Assets.hudPatch.draw(
            batch,
            hudBorderWidth,
            hudCamera.viewportHeight - (hudHeight + hudBorderWidth),
            hudCamera.viewportWidth - (hudBorderWidth * 2),
            hudHeight
        );
        Assets.drawString(
            batch,
            "Player    Deaths",
            hudBorderWidth + Assets.hudPatch.getPadLeft(),
            hudCamera.viewportHeight - Assets.hudPatch.getPadTop(),
            Color.WHITE,
            .4f
        );
        Assets.drawString(
            batch,
            Integer.toString(Statistics.deaths),
            hudBorderWidth + Assets.hudPatch.getPadLeft() + 150,
            hudCamera.viewportHeight - (Assets.hudPatch.getPadTop() + 30),
            Color.WHITE,
            .4f
        );

        if (level.boss.activated && !level.boss.dead) {
            Assets.font.getData().setScale(.4f);
            Assets.glyphLayout.setText(Assets.font, level.boss.name);
            Assets.drawString(
                    batch,
                    level.boss.name,
                    hudCamera.viewportWidth - 100 - hudBorderWidth + Assets.hudPatch.getPadLeft() - Assets.glyphLayout.width,
                    hudCamera.viewportHeight - Assets.hudPatch.getPadTop(),
                    Color.WHITE,
                    .4f
            );

            batch.setColor(Color.BLACK);
            batch.draw(
                    Assets.white,
                    hudCamera.viewportWidth - 200 - hudBorderWidth + Assets.hudPatch.getPadLeft(),
                    hudCamera.viewportHeight - 75,
                    100, // Full health
                    15
            );

            float n = level.boss.health / (float) level.boss.maxHealth;
            healthColor = Utils.hsvToRgb(((n * 120f) - 20) / 365f, 1.0f, 1.0f, healthColor);
            batch.setColor(healthColor);
            batch.draw(
                    Assets.white,
                    hudCamera.viewportWidth - 200 - hudBorderWidth + Assets.hudPatch.getPadLeft(),
                    hudCamera.viewportHeight - 75,
                    (float)level.boss.health/ level.boss.maxHealth * 100f,
                    15
            );
            batch.setColor(Color.WHITE);
        }

        // Draw Player Health bar
        batch.setColor(Color.BLACK);
        batch.draw(
            Assets.white,
            hudBorderWidth + Assets.hudPatch.getPadLeft(),
            hudCamera.viewportHeight - 75,
            debugPlayer.maxHealth, // Full health
            15
        );

        float n = debugPlayer.health / (float) debugPlayer.maxHealth;
        healthColor = Utils.hsvToRgb(((n * 120f) - 20) / 365f, 1.0f, 1.0f, healthColor);
        batch.setColor(healthColor);
        batch.draw(
            Assets.white,
            hudBorderWidth + Assets.hudPatch.getPadLeft(),
            hudCamera.viewportHeight - 75,
            debugPlayer.health,
            15
        );
        batch.setColor(Color.WHITE);

        batch.end();
    }

}
