package lando.systems.ld36.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Sort;
import lando.systems.ld36.entities.*;
import lando.systems.ld36.screens.GameScreen;
import lando.systems.ld36.utils.Assets;

import java.util.Comparator;

import static lando.systems.ld36.levels.Level.MapObjectType.*;

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
    public Player player;

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
                if (!enemy.isHurt) {
                    int dir = player.doesHit(enemy);
                    if (dir != 0) {
                        enemy.getHurt(player.attackPower, dir);
                    }
                }

                if (enemy.dead) {
                    // TODO: spawn cool explosion thing
                }
            }
        }

        for (int i = objects.size -1; i>=0; i--){
            GameObject object = objects.get(i);
            if (!(object instanceof Enemy)) continue;

            final Enemy enemy = (Enemy) object;
            if (enemy.dead) {
                objects.removeIndex(i);
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

    enum MapObjectType {
        player(),
        flash_drive_easy(),
        flash_drive_medium(),
        flash_drive_hard();
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

            switch (valueOf(type)) {
                case player:
                    Player p = new Player(this);
                    p.position.x = x;
                    p.position.y = y;
                    objects.add(p);
                    break;

                case flash_drive_easy:
                    objects.add(new FlashDriveEasy(this, x, y));
                    break;

                case flash_drive_medium:
                    objects.add(new FlashDriveMedium(this, x, y));
                    break;

                case flash_drive_hard:
                    objects.add(new FlashDriveHard(this, x, y));
                    break;
            }
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
