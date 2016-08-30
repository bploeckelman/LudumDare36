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
            case 2:
                return new com.badlogic.gdx.utils.StringBuilder()
                        .append("And our heros are underway.\n\n")
                        .append("Having defeated the first of the smart phones they continue on.")
                        .toString();
            case 3:
                return new com.badlogic.gdx.utils.StringBuilder()
                        .append("Already killing many SD cards and Flash drives.\n\nThey continue ")
                        .append("their quest to find Cass-ette and claim victory for the COOT \"COUNCIL ")
                        .append("OF OBSOLETE TECHNOLOGIES.\"")
                        .toString();

            case 4:
                return new com.badlogic.gdx.utils.StringBuilder()
                        .append("Now the heros find themselves in a room made of a series of tubes.\n")
                        .append("They must be getting close to their goal. (hint: this is the final level)")
                        .toString();
            case 5:
                return new com.badlogic.gdx.utils.StringBuilder()
                        .append("As you might have already guessed. Our heros defeated \"The Cloud\" ")
                        .append("and saved Cass-ette.\n\n\n")
                        .append("It turns out the Cloud was just somebody elses computer.")
                        .toString();
        }
        return "Oops we missed a text segment";
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
