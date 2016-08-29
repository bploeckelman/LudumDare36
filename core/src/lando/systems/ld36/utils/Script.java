package lando.systems.ld36.utils;

/**
 * Created by dsgraham on 8/29/16.
 */
public class Script {
    public static String getScript(int level){
        switch (level){
            case 1:
                return new com.badlogic.gdx.utils.StringBuilder()
                        .append("* ASDW / Arrows to move\n")
                        .append("* ENTER to punch")
                        .append("\n\n\n")
                        .append("The mighty technologies of yester-year have fallen into the heap ")
                        .append("in humanities wake.")
                        .append("\n\n\nThe evil internet is poised to destroy another ")
                        .append("member of the COUNCIL OF OBSOLETE TECHNOLOGIES.")
                        .append("\n\n\nWe must save Cass-ette...\n")
                        .append("or she will be lost to history.")
                        .toString();
        }
        return "You didn't write a script yet... ";
    }

    public static String getLevelFileName(){
        switch (Statistics.currentLevel){
            case 1: return "levels/level1.tmx";
            case 2: return "levels/level2.tmx";
            case 3: return "levels/level3.tmx";
            case 4: return "levels/level4.tmx";
            default: return null;
        }
    }
}
