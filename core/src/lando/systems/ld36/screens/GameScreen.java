package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.utils.Config;

import static lando.systems.ld36.utils.Assets.batch;

/**
 * Created by Brian on 8/27/2016.
 */
public class GameScreen extends BaseScreen {

    TiledMap map;
    TiledMapRenderer mapRenderer;
    TiledMapTileLayer groundLayer;
    TiledMapImageLayer backgroundLayer;

    public GameScreen() {
        loadMap("levels/level0.tmx");
        camera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        camera.update();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare36.game.screen = new MenuScreen();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        {
            mapRenderer.renderImageLayer(backgroundLayer);
            mapRenderer.renderTileLayer(groundLayer);
        }
        batch.end();
    }

    private void loadMap(String mapName){
        final TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load(mapName);
//        loadMapObjects();

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f, batch);

        backgroundLayer = (TiledMapImageLayer) map.getLayers().get("background");
        groundLayer = (TiledMapTileLayer) map.getLayers().get("ground");
    }

}
