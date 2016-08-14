package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Config;

public class MenuScreen extends BaseScreen {

    private SpriteBatch batch;
    private Texture img;

    public MenuScreen() {
        batch = Assets.batch;
        img = Assets.mgr.get("images/spritesheet.png", Texture.class);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img,
                Gdx.graphics.getWidth()  / 2f - img.getWidth()  / 2f,
                Gdx.graphics.getHeight() / 2f - img.getHeight() / 2f);
        batch.end();
    }

}
