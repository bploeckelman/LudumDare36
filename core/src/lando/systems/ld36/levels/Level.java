package lando.systems.ld36.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.utils.Assets;

/**
 * Created by Brian on 8/27/2016.
 */
public class Level {

    public TiledMap map;
    TiledMapRenderer renderer;
    TiledMapTileLayer groundLayer;
    Array<TiledMapImageLayer> imageLayers;

    Player player;

    public Level(String name) {
        load(name, Assets.batch);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void update(float dt) {
        player.update(dt);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        renderer.setView(camera);
        for (TiledMapImageLayer imageLayer : imageLayers) {
            renderer.renderImageLayer(imageLayer);
        }
        renderer.renderTileLayer(groundLayer);

        player.render(batch);
    }

    private void load(String mapName, SpriteBatch batch){
        final TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load(mapName);
//        loadMapObjects();

        renderer = new OrthogonalTiledMapRenderer(map, 1f, batch);

        groundLayer = (TiledMapTileLayer) map.getLayers().get("ground");

        imageLayers = new Array<TiledMapImageLayer>();
        final MapLayers mapLayers = map.getLayers();
        for (MapLayer layer : mapLayers) {
            if (layer.getName().startsWith("background")) {
                final TiledMapImageLayer imageLayer = (TiledMapImageLayer) layer;
                final MapProperties props = layer.getProperties();
                imageLayer.setX(imageLayer.getX() + Float.parseFloat(props.get("offset_x", "0", String.class)));
                imageLayer.setY(imageLayer.getY() + Float.parseFloat(props.get("offset_y", "0", String.class)));
                imageLayers.add(imageLayer);
            }
        }
    }

}
