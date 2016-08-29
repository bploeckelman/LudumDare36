package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.entities.PlayerCharacter;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Config;
import lando.systems.ld36.utils.Shake;
import lando.systems.ld36.utils.Utils;
import lando.systems.ld36.utils.Statistics;

/**
 * Created by Brian on 8/27/2016.
 */
public class GameScreen extends BaseScreen {

    Player debugPlayer;
    Level level;
    float hudHeight = 100;
    float hudBorderWidth = 4;
    public Shake screenShake;
    public Vector2 cameraCenter;
    public Color healthColor;

    public GameScreen(PlayerCharacter character) {
        camera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        camera.update();
        screenShake = new Shake(35, 8);
        level = new Level("levels/level1.tmx", this);
        debugPlayer = new Player(character, level);
        level.setPlayer(debugPlayer);
        level.initilizeStates();
        cameraCenter = new Vector2(camera.position.x, camera.position.y);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare36.game.setScreen(Statistics.getStatisticsScreen());
        }
        debugPlayer.update(dt, cameraCenter.x - camera.viewportWidth/2);

        // have camera follow player
        if (debugPlayer.position.x > cameraCenter.x + camera.viewportWidth /6){
            float screenXDif = debugPlayer.position.x - cameraCenter.x - camera.viewportWidth /6;
            cameraCenter.x += screenXDif * .1f;
        }

        if (cameraCenter.x > level.getLevelWidth() - (camera.viewportWidth/2))  {
            cameraCenter.x = level.getLevelWidth() - (camera.viewportWidth/2);
        }

//        camera.update();
        screenShake.update(dt, camera, cameraCenter);

        level.update(dt);
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
        Assets.drawString(
            batch,
            "Enemy",
            hudCamera.viewportWidth - 100 - hudBorderWidth - Assets.hudPatch.getPadRight(),
            hudCamera.viewportHeight - Assets.hudPatch.getPadTop(),
            Color.WHITE,
            .4f
        );

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
