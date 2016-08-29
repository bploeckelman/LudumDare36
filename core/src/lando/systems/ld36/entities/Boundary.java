package lando.systems.ld36.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Brian on 8/28/2016.
 */
public class Boundary {

    public Vector2 position;
    public boolean enabled;

    public Boundary(Vector2 position) {
        this.position = position;
        this.enabled = true;
    }

    public Boundary(float x, float y) {
        this.position = new Vector2(x, y);
        this.enabled = true;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

}
