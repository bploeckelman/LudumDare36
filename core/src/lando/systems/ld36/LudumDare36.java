package lando.systems.ld36;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import lando.systems.ld36.screens.BaseScreen;
import lando.systems.ld36.screens.CharacterSelectScreen;
import lando.systems.ld36.screens.MenuScreen;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Config;

public class LudumDare36 extends ApplicationAdapter {

    public static LudumDare36 game;

    public BaseScreen screen;
    private FrameBuffer currentFBO;
    private OrthographicCamera screenCamera;


    @Override
	public void create () {
        Assets.load();

        float aspect = Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        screenCamera = new OrthographicCamera(Config.gameWidth, Config.gameWidth / aspect);
        screenCamera.translate(screenCamera.viewportWidth / 2, screenCamera.viewportHeight / 2, 0);
        screenCamera.update();

        currentFBO = new FrameBuffer(Pixmap.Format.RGB888, Config.gameWidth, Config.gameHeight, false);

        float progress = 0f;
        do {
            progress = Assets.update();
        } while (progress != 1f);
        game = this;
        screen = new MenuScreen();
    }

    @Override
    public void resume() {
        // May not want this here.
//        Assets.load();
        game = this;
    }

    @Override
    public void dispose() {
        Assets.dispose();
    }

	@Override
	public void render () {
        float dt = Math.min(Gdx.graphics.getDeltaTime(), 1f / 60f);
        Assets.tween.update(dt);
        screen.update(dt);


        currentFBO.begin();

        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        screen.render(Assets.batch);

        currentFBO.end();

        Texture currentTexture = currentFBO.getColorBufferTexture();
        currentTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        Assets.batch.setShader(Assets.crtShader);
        Assets.crtShader.begin();
        Assets.crtShader.setUniformf("u_kWarp", 1/16f, 1/16f);
        Assets.batch.setProjectionMatrix(screenCamera.combined);
        Assets.batch.begin();
        Assets.batch.draw(currentTexture, 0, currentFBO.getHeight(), currentFBO.getWidth(), -currentFBO.getHeight());
        Assets.batch.end();
        Assets.crtShader.end();
        Assets.batch.setShader(null);


    }

}
