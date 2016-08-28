package lando.systems.ld36.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Sort;
import lando.systems.ld36.entities.*;
import lando.systems.ld36.screens.GameScreen;
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
    public GameScreen screen;
    Player player;

    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };

    Comparator<GameObject> gameObjectYPosComparator = new Comparator<GameObject>() {
        @Override
        public int compare(GameObject o1, GameObject o2) {
            if      (o1.position.y == o2.position.y) return 0;
            else if (o1.position.y >  o2.position.y) return -1;
            else                                     return 1;
        }
    };

    public Level(String name, GameScreen screen) {
        this.screen = screen;
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

        Sort.instance().sort(objects, gameObjectYPosComparator);

        if (player.isAttacking) {
            for (int i = objects.size - 1; i >= 0; --i) {
                GameObject object = objects.get(i);
                if (!(object instanceof Enemy)) continue;

                final Enemy enemy = (Enemy) object;
                if (!enemy.isHurt && player.doesHit(enemy)) {
                    enemy.getHurt(1);
                }

                if (enemy.dead) {
                    // TODO: spawn cool explosion thing
                    objects.removeIndex(i);
                }
            }
        }

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
                Player p = new Player(this);
                p.position.x = x;
                p.position.y = y;
                objects.add(p);
            }
            else if (type.equals("flashdrive")) {
                FlashDrive f = new FlashDrive(this);
                f.position.x = x;
                f.position.y = y;
                f.isMoving = true;
                objects.add(f);
            }
            else if (type.equals("eighttrack")) {
                EightTrack t = new EightTrack(this);
                t.position.x = x;
                t.position.y = y;
                t.isMoving = true;
                objects.add(t);
            }
            else if (type.equals("betamax")) {
                Betamax b = new Betamax(this);
                b.position.x = x;
                b.position.y = y;
                b.isMoving = true;
                objects.add(b);
            }
//            else if (type.equals("...")) {
//                new GameObject(this, x / 16, (y / 16) + 1, false);
//            }
        }
    }

    public float getLevelWidth(){
        return groundLayer.getWidth() * groundLayer.getTileWidth();
    }

    public void getGroundTiles (Vector3 position, Array<Rectangle> tiles) {
        int startX = (int)(position.x / groundLayer.getTileWidth()) -1;
        int startY = (int)(position.y / groundLayer.getTileHeight()) -1;
        int endX = startX + 4;
        int endY = startY + 4;

        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x * 32, y * 32, 32, 32);
                    tiles.add(rect);
                }
            }
        }
    }

}
