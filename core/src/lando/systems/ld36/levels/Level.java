package lando.systems.ld36.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Sort;
import lando.systems.ld36.ai.states.WaitState;
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
    public Array<Boundary> boundaries;
    TiledMapRenderer renderer;
    TiledMapTileLayer groundLayer;
    Array<TiledMapImageLayer> imageLayers;
    Array<TiledMapTileLayer> backgroundLayers;
    public GameScreen screen;
    public Player player;
    public GameObject boss;
    public boolean completed;

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

    public Level(String name, PlayerCharacter character, GameScreen screen) {
        this.screen = screen;
        load(name, character, Assets.batch);
        completed = false;
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
                if (!enemy.isInvulerable()) {
                    int dir = player.doesHit(enemy);
                    if (dir != 0) {
                        enemy.getHurt(player.attackPower, dir);
                    }
                }

                if (enemy.dead) {
                }
            }
        }

        int aliveActiveEnemiesCount = 0;
        for (int i = objects.size -1; i>=0; i--){
            GameObject object = objects.get(i);
            if (!(object instanceof Enemy)) continue;

            final Enemy enemy = (Enemy) object;
            if (!player.isInvulerable() && enemy.isAttacking){
                int dir = enemy.doesHit(player);
                if  (dir != 0){
                    Assets.particles.addBloodParticles(player.hitBounds, dir, enemy.attackPower);
                    player.getHurt(enemy.attackPower, dir);
                }
            }
            if (enemy.activated){
                aliveActiveEnemiesCount++;
            }
            if (enemy.dead) {
                objects.removeIndex(i);
            }
        }
        Boundary b = getActiveBoundry();
        if (b != null && aliveActiveEnemiesCount <= 0){
            screen.lagdelay = 10;
            screen.cameraDelay += .9f;
            b.disable();
        }

        if (!completed && boss.dead && aliveActiveEnemiesCount <= 0){
            completed = true;
            screen.cameraDelay += 3f;
        }



    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        renderer.setView(camera);
        for (TiledMapImageLayer imageLayer : imageLayers) {
            renderer.renderImageLayer(imageLayer);
        }
        for (TiledMapTileLayer backgroundLayer : backgroundLayers) {
            renderer.renderTileLayer(backgroundLayer);
        }
        renderer.renderTileLayer(groundLayer);

        for (GameObject object : objects) {
            object.render(batch);
        }
    }

    private void load(String mapName, PlayerCharacter character, SpriteBatch batch){
        final TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load(mapName);
        loadMapObjects(character);

        renderer = new OrthogonalTiledMapRenderer(map, 1f, batch);

        groundLayer = (TiledMapTileLayer) map.getLayers().get("ground");

        imageLayers = new Array<TiledMapImageLayer>();
        backgroundLayers = new Array<TiledMapTileLayer>();
        final MapLayers mapLayers = map.getLayers();
        for (MapLayer layer : mapLayers) {
            if (layer.getName().startsWith("background")) {
                if (layer instanceof TiledMapImageLayer) {
                    final TiledMapImageLayer imageLayer = (TiledMapImageLayer) layer;
                    final MapProperties props = layer.getProperties();
                    imageLayer.setX(imageLayer.getX() + Float.parseFloat(props.get("offset_x", "0", String.class)));
                    imageLayer.setY(imageLayer.getY() + Float.parseFloat(props.get("offset_y", "0", String.class)));
                    imageLayers.add(imageLayer);
                }
                else if (layer instanceof TiledMapTileLayer) {
                    final TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
                    backgroundLayers.add(tileLayer);
                }
            }
        }
    }

    enum MapObjectType {
        boundary(),
        player(),
        flash_drive_easy(),
        flash_drive_medium(),
        flash_drive_hard(),
        smart_phone_easy(),
        smart_phone_medium(),
        smart_phone_hard(),
        cloud(),
        sd_easy(),
        sd_medium(),
        sd_hard();
    }

    private void loadMapObjects(PlayerCharacter character) {
        if (map == null) return;

        objects = new Array<GameObject>();
        boundaries = new Array<Boundary>();

        MapProperties props;
        MapLayer objectLayer = map.getLayers().get("objects");
        for (MapObject object : objectLayer.getObjects()) {
            props = object.getProperties();
            boolean isBoss = (props.get("boss") != null);
            float w = (Float) props.get("width");
            float h = (Float) props.get("height");
            float x = (Float) props.get("x");
            float y = (Float) props.get("y");
            String type = (String) props.get("type");

            switch (valueOf(type)) {
                case boundary:
                    boundaries.add(new Boundary(x, y));
                    break;

                case player:
                    player = new Player(character, this);
                    player.position.x = x;
                    player.position.y = y;
                    objects.add(player);
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

                case smart_phone_easy:
                    objects.add(new SmartPhoneEasy(this, x, y, isBoss));
                    break;

                case smart_phone_medium:
                    objects.add(new SmartPhoneMedium(this, x, y, isBoss));
                    break;

                case smart_phone_hard:
                    objects.add(new SmartPhoneHard(this, x, y, isBoss));
                    break;

                case cloud:
                    objects.add(new Cloud(this, x, y, isBoss));
                    break;

                case sd_easy:
                    objects.add(new SDEasy(this, x, y));
                    break;

                case sd_medium:
                    objects.add(new SDMedium(this, x, y));
                    break;

                case sd_hard:
                    objects.add(new SDHard(this, x, y));
                    break;
            }
        }

    }

    public void initilizeStates(){
        for (GameObject object : objects){
            object.initializeStates();
        }
    }

    public float getLevelWidth(){
        return groundLayer.getWidth() * groundLayer.getTileWidth();
    }

    public void getGroundTiles (Vector3 position, Array<Rectangle> tiles) {
        int startX = (int)(position.x / groundLayer.getTileWidth()) -1;
        int startY = (int)(position.y / groundLayer.getTileHeight()) -1;
        int endX = startX + 5;
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

    public float getTopBound(float position){
        int startX = (int)(position / groundLayer.getTileWidth());
        int startY = 0;
        int endX = startX;
        int endY = 40; // something higher than the upper bound

        float topBound = 0;
        for (int x = startX; x <= endX; x++) {
            float topBoundInX = 0;
            for (int y = startY; y <= endY; y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                if (cell != null) {
                    topBoundInX = Math.max(topBoundInX, y * 32 + 16);
                }
            }
            topBound = Math.max(topBound, topBoundInX);
        }
        return topBound;
    }

    public Boundary getActiveBoundry(){
        float cameraRightEdge = screen.cameraCenter.x + screen.camera.viewportHeight / 2f;
        for (Boundary b : boundaries){
            if (b.enabled && b.position.x < cameraRightEdge) return b;
        }
        return null;
    }

    public int activeBoundries(){
        int count = 0;
        for (Boundary b : boundaries){
            if (b.enabled) count++;
        }
        return count;
    }

    public int getActiveEnemies(){
        int count = 0;
        for (int i = objects.size -1; i>=0; i--) {
            GameObject object = objects.get(i);
            if (!(object instanceof Enemy)) continue;

            final Enemy enemy = (Enemy) object;
            if (enemy.activated) count++;

        }
        return count;
    }

}
