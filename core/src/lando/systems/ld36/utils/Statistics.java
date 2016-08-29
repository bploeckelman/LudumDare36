package lando.systems.ld36.utils;

import com.badlogic.gdx.utils.StringBuilder;
import lando.systems.ld36.screens.TextScreen;

public class Statistics {
    public static int punchesThrown = 0;
    public static int damageDealt = 0;
    public static int damageTaken = 0;
    public static int deaths = 0;
    public static int enemiesKilled = 0;

    public static TextScreen getStatisticsScreen() {
        return new TextScreen(new StringBuilder()
            .append("Punches Thrown: " + Integer.toString(punchesThrown) + "\n")
            .append("Damage Dealt: " + Integer.toString(damageDealt) + "\n")
            .append("Damage Taken: " + Integer.toString(damageTaken) + "\n")
            .append("Deaths: " + Integer.toString(deaths) + "\n")
            .append("Enemies Killed: " + Integer.toString(enemiesKilled) + "\n")
            .append("\n\nGAME OVER\n\n")
            .append("Made for Ludum Dare by:\n\n")
            .append("Doug Graham\n")
            .append("Brian Ploeckelman\n")
            .append("Colin Kennedy\n")
            .append("Ian McNamara\n")
            .append("Alex Harding\n")
            .append("Luke Bain\n")
            .append("Troy Sullivan")
            .toString()
        );
    }
}
