package lando.systems.ld36;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import lando.systems.ld36.screens.BaseScreen;
import lando.systems.ld36.screens.MenuScreen;
import lando.systems.ld36.utils.Assets;

public class LudumDare36 extends ApplicationAdapter {

    public static LudumDare36 game;

    public BaseScreen screen;

	@Override
	public void create () {
        Assets.load();
        float progress = 0f;
        do {
            progress = Assets.update();
        } while (progress != 1f);
        game = this;
        screen = new MenuScreen();
    }

    @Override
    public void resume() {
        Assets.load();
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
        screen.render(Assets.batch);
	}

}
