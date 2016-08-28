package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Config;

/**
 * Created by Brian on 8/27/2016.
 */
public class GameScreen extends BaseScreen {

    Player debugPlayer;
    Level level;
    float hudHeight = 100;
    float hudBorderWidth = 4;

    public GameScreen() {
        camera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        camera.update();
        level = new Level("levels/level0.tmx");
        debugPlayer = new Player(level);
        level.setPlayer(debugPlayer);

    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare36.game.screen = new MenuScreen();
        }
        debugPlayer.update(dt, camera.position.x - camera.viewportWidth/2);

        // have camera follow player
        if (debugPlayer.position.x > camera.position.x - debugPlayer.width){
            float screenXDif = debugPlayer.position.x - camera.position.x + debugPlayer.width;
            camera.position.x += screenXDif * .05f;
        }

        if (camera.position.x > level.getLevelWidth() - (camera.viewportWidth/2))  {
            camera.position.x = level.getLevelWidth() - (camera.viewportWidth/2);
        }

        camera.update();

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
            Integer.toString(debugPlayer.deaths),
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
            100, // Full health
            15
        );
        batch.setColor(Color.RED);
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
