package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.levels.Level;
import lando.systems.ld36.utils.Config;

/**
 * Created by Brian on 8/27/2016.
 */
public class GameScreen extends BaseScreen {

    Player debugPlayer;
    Level level;

    public GameScreen() {
        camera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        camera.update();
        level = new Level("levels/level0.tmx");
        debugPlayer = new Player();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare36.game.screen = new MenuScreen();
        }
        debugPlayer.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        {
            level.render(batch, camera);
            debugPlayer.render(batch);
        }
        batch.end();
    }

}
