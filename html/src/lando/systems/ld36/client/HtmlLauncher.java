package lando.systems.ld36.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.TimeUtils;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
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

        long loadStart = TimeUtils.nanoTime();
        public Preloader.PreloaderCallback getPreloaderCallback () {
                final Canvas canvas = Canvas.createIfSupported();
                canvas.setWidth("" + (Config.gameWidth) + "px");
                canvas.setHeight("200px");
                getRootPanel().add(canvas);
                final Context2d context = canvas.getContext2d();
                context.setTextAlign(Context2d.TextAlign.CENTER);
                context.setTextBaseline(Context2d.TextBaseline.MIDDLE);
                context.setFont("10pt Calibri");
                return new Preloader.PreloaderCallback() {

                        @Override
                        public void update(Preloader.PreloaderState state) {
                                long nowTime = TimeUtils.nanoTime();
                                long difTime = nowTime - loadStart;
                                difTime /= 1000000000;

                                int dots = (int)difTime % 5;
                                String color = "black";
                                context.setFillStyle(color);
                                context.setStrokeStyle(color);
                                context.fillRect(0, 0, 300, 200);
//                                color = "green";
//                                context.setFillStyle(color);
//                                context.setStrokeStyle(color);
//                                context.fillRect(0, 0, 300 * state.getProgress() * 0.97f, 70);

                                context.setFillStyle("green");
                                String dotString = "";
                                for (int i = 0; i < dots; i++){
                                        dotString += ".";
                                }
                                context.fillText("loading"+dotString, 300 / 2, 100);
                        }

                        @Override
                        public void error (String file) {
                                System.out.println("error: " + file);
                        }
                };
        }
}