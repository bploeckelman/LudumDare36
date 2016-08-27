package lando.systems.ld36.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import lando.systems.ld36.entities.GameObject;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.utils.Assets;

import java.util.Comparator;

/**
 * Created by Brian on 8/27/2016.
 */
public class Level {

    public TiledMap map;
    public Array<GameObject> objects;
    TiledMapRenderer renderer;
    TiledMapTileLayer groundLayer;
    Array<TiledMapImageLayer> imageLayers;

    Player player;

    Comparator<GameObject> gameObjectYPosComparator = new Comparator<GameObject>() {
        @Override
        public int compare(GameObject o1, GameObject o2) {
            if      (o1.position.y == o2.position.y) return 0;
            else if (o1.position.y >  o2.position.y) return -1;
            else                                     return 1;
        }
    };

    public Level(String name) {
        load(name, Assets.batch);
    }

    public void setPlayer(Player player) {
        this.player = player;
        objects.add(this.player);
    }

    public void update(float dt) {
        for (GameObject object : objects) {
            object.update(dt);
        }
        player.update(dt);

        Sort.instance().sort(objects, gameObjectYPosComparator);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        renderer.setView(camera);
        for (TiledMapImageLayer imageLayer : imageLayers) {
            renderer.renderImageLayer(imageLayer);
        }
        renderer.renderTileLayer(groundLayer);

        for (GameObject object : objects) {
            object.render(batch);
        }
    }

    private void load(String mapName, SpriteBatch batch){
        final TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load(mapName);
        loadMapObjects();

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

    private void loadMapObjects() {
        if (map == null) return;

        objects = new Array<GameObject>();

        MapProperties props;
        MapLayer objectLayer = map.getLayers().get("objects");
        for (MapObject object : objectLayer.getObjects()) {
            props = object.getProperties();
            float w = (Float) props.get("width");
            float h = (Float) props.get("height");
            float x = (Float) props.get("x");
            float y = (Float) props.get("y");
            String type = (String) props.get("type");

            // Instantiate based on type
            if (type.equals("player")) {
                Player p = new Player();
                p.position.x = x;
                p.position.y = y;
                objects.add(p);
            }
//            else if (type.equals("...")) {
//                new GameObject(this, x / 16, (y / 16) + 1, false);
//            }
        }
    }

}
