package lando.systems.ld36.utils;

import com.badlogic.gdx.utils.*;

/**
 * Created by dsgraham on 8/29/16.
 */
public class Script {
    public static String getScript(int level){
        switch (level){
            case 1:
                return new com.badlogic.gdx.utils.StringBuilder()
                        .append("The mighty technologies of yesteryear have fallen into the heap ")
                        .append("in humanities wake.  The evil internet is poised to destroy another ")
                        .append("of COUNCIL OF OBSOLETE TECHNOLOGIES.  We must save Tapetress... or ")
                        .append("she will be lost to history.")
                        .toString();
        }
        return "You didn't write a script yet... ";
    }

    public static String getLevelFileName(){
        switch (Statistics.currentLevel){
            case 1:
                return "levels/level1.tmx";
            case 2:
                return "levels/level2.tmx";
            case 3:
                return "levels/level0.tmx";
        }

        return null;
    }
}
