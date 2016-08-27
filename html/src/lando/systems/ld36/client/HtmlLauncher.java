package lando.systems.ld36.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.utils.Config;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Config.gameWidth, Config.gameHeight);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new LudumDare36();
        }
}