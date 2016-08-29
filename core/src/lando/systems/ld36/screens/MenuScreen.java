package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.entities.PlayerCharacter;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Config;
import lando.systems.ld36.utils.Script;

public class MenuScreen extends BaseScreen {

    private Texture img;

    public MenuScreen() {
        img = Assets.mgr.get("images/title-screen.png", Texture.class);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            LudumDare36.game.setScreen(new TextScreen(Script.getScript(1), new CharacterSelectScreen()
            ));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(img, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
    }

}
