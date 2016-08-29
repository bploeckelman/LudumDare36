package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.CharArray;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Script;
import lando.systems.ld36.utils.Statistics;

public class TextScreen extends BaseScreen {

    CharArray text;
    String textToRender = "";
    float timer;
    float border = 4;
    float typingRate;
    Rectangle panel;
    Rectangle textBounds;
    BaseScreen nextScreen;

    public TextScreen(String text, BaseScreen screen) {
        this(text, .07f, screen);
    }

    public TextScreen(String text, float typingRate, BaseScreen screen) {
        super();
        nextScreen = screen;
        this.text = CharArray.with(text.toCharArray());
        this.text.reverse();
        panel = new Rectangle(border, border, camera.viewportWidth - (border * 2), camera.viewportHeight - (border * 2));
        textBounds = new Rectangle(
            panel.x + Assets.hudPatch.getPadLeft(),
            panel.y + Assets.hudPatch.getPadBottom(),
            panel.width - (Assets.hudPatch.getPadLeft() * 2),
            panel.height - (Assets.hudPatch.getPadBottom() * 2)
        );

        this.typingRate = typingRate;
    }

    @Override
    public void update(float dt) {
        timer += dt;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && text.size == 0 && nextScreen != null) {
            if (Script.getLevelFileName() == null){
                LudumDare36.game.setScreen(Statistics.getStatisticsScreen());
            } else {
                LudumDare36.game.setScreen(nextScreen);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && text.size > 0) {
            while (text.size > 0) {
                textToRender += text.pop();
            }
        }

        if (timer >= typingRate && text.size > 0) {
            timer = 0f;
            textToRender += text.pop();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        Assets.hudPatch.draw(batch, panel.x, panel.y, panel.width, panel.height);
        Assets.emuLogicFont.getData().setScale(1f);
        Assets.glyphLayout.setText(Assets.emuLogicFont, textToRender, Color.WHITE, textBounds.width, Align.left, true);
        Assets.emuLogicFont.draw(batch, Assets.glyphLayout, textBounds.x, textBounds.y + textBounds.height);

        batch.end();
    }
}
