package lando.systems.ld36.utils.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld36.utils.Assets;

/**
 * Created by Brian on 8/28/2016.
 */
public class ParticleManager {

    private final Array<Particle> activeParticles = new Array<Particle>();
    private final Pool<Particle> particlePool = Pools.get(Particle.class, 100);

    private final TextureRegion[][] regions;
    private final int numRegionsX;
    private final int numRegionsY;

    public ParticleManager() {
        regions = TextureRegion.split(Assets.mgr.get("images/pcb-full.png", Texture.class), 32, 32);
        numRegionsY = regions.length;
        numRegionsX = regions[0].length;
    }

    public void addParticle(Rectangle bounds, Color c){
        int tiles = 10;
        float boundDx = bounds.width / tiles;
        for (int x = 0; x < tiles; x++){
            for (int y = 0; y < tiles; y++){
                Particle part = particlePool.obtain();

                float speed = MathUtils.random(300f);
                float dir = MathUtils.random(360f);
                float px = bounds.x + x * boundDx;
                float py = bounds.y + (y-1) * boundDx;
                float vx = MathUtils.sinDeg(dir) * speed;
                float vy = MathUtils.cosDeg(dir) * speed;
                float scale = MathUtils.random(boundDx, 4f * boundDx);
                float ttl = MathUtils.random(0.5f, 2f);
                part.init(
                        px, py,
                        vx, vy,
                        -vx, -vy,
                        c.r, c.g, c.b, c.a,
                        c.r, c.g, c.b, 0f,
                        scale, ttl,
                        getRandomTextureRegion());

                activeParticles.add(part);
            }
        }
    }

    public void update(float dt){
        int len = activeParticles.size;
        for (int i = len -1; i >= 0; i--){
            Particle part = activeParticles.get(i);
            part.update(dt);
            if (part.timeToLive <= 0){
                activeParticles.removeIndex(i);
                particlePool.free(part);
            }
        }
    }

    public void render(SpriteBatch batch){
        for (Particle part : activeParticles){
            part.render(batch);
        }
    }

    private TextureRegion getRandomTextureRegion() {
        return regions[MathUtils.random(numRegionsY - 1)][MathUtils.random(numRegionsX - 1)];
    }

}
