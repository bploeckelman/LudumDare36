package lando.systems.ld36.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by dsgraham on 8/27/16.
 */
public class KeyMapping {
    public enum ACTION {JUMP, LEFT, RIGHT, UP, DOWN}

    public ObjectMap<ACTION, Integer> actionMap;

    public KeyMapping(){
        actionMap = new ObjectMap<ACTION, Integer>();
        actionMap.put(ACTION.JUMP, Input.Keys.SPACE);
        actionMap.put(ACTION.LEFT, Input.Keys.A);
        actionMap.put(ACTION.RIGHT, Input.Keys.D);
        actionMap.put(ACTION.UP, Input.Keys.W);
        actionMap.put(ACTION.DOWN, Input.Keys.S);
    }

    public boolean isActionPressed(ACTION action){
        return Gdx.input.isKeyPressed(actionMap.get(action));
    }

}
