package lando.systems.ld36.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.utils.Config;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Config.gameWidth;
		config.height = Config.gameHeight;
		config.resizable = false;
		new LwjglApplication(new LudumDare36(), config);
	}
}
