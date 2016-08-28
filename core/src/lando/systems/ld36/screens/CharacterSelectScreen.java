package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.entities.PlayerCharacter;
import lando.systems.ld36.utils.Assets;

public class CharacterSelectScreen extends BaseScreen {
    float border = 4f;
    Rectangle topPanel;
    Array<Rectangle> characterPanels;
    float timer = 0f;
    int hoverCharacter = -1;

    public CharacterSelectScreen() {
        super();

        // Top panel takes the entire screen width and 1/4 the height
        topPanel = new Rectangle(
            border,
            camera.viewportHeight - (camera.viewportHeight/4),
            camera.viewportWidth - (border * 2),
            (camera.viewportHeight/4) - (border * 2)
        );


        // Character panels take 1/3 of the width of the screen and 3/4 the height
        characterPanels = new Array<Rectangle>();
        float characterPanelWidth = (camera.viewportWidth - (border * 4)) / 3;
        for (int i = 0; i < 3; i++) {
            characterPanels.add(new Rectangle(
                border + (i * characterPanelWidth) + (i * border),
                border,
                characterPanelWidth,
                (camera.viewportHeight / 4) * 3 - (border * 2)
            ));
        }
    }

    @Override
    public void update(float dt) {
        timer += dt;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        PlayerCharacter[] characters = PlayerCharacter.values();
        Vector3 cursorPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        hudCamera.unproject(cursorPoint);

        hoverCharacter = -1;
        for (int i = 0; i < characterPanels.size; i++) {
            if (characterPanels.get(i).contains(cursorPoint.x, cursorPoint.y)) {
                hoverCharacter = i;
                break;
            }
        }

        if (Gdx.input.justTouched() && hoverCharacter >= 0) {
            LudumDare36.game.screen = new GameScreen(characters[hoverCharacter]);
        }

        if (hoverCharacter >= 0) {
            characters[hoverCharacter].keyframe = characters[hoverCharacter].walkAnimation.getKeyFrame(timer);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        Assets.hudPatch.draw(batch, topPanel.x, topPanel.y, topPanel.width, topPanel.height);

        Assets.drawString(
            batch,
            "Select your fighter",
            topPanel.x + Assets.hudPatch.getPadLeft(),
            topPanel.y + Assets.hudPatch.getPadBottom() + 50,
            Color.WHITE,
            .7f
        );

        PlayerCharacter[] characters = PlayerCharacter.values();
        for (int i = 0; i < characterPanels.size; i++) {
            Rectangle p = characterPanels.get(i);
            Assets.hudPatch.draw(batch, p.x, p.y, p.width, p.height);
            batch.draw(
                characters[i].keyframe,
                p.x + (p.width / 2) - (characters[i].keyframe.getRegionWidth() / 2) - 20,
                p.y + (p.height / 2),
                characters[i].keyframe.getRegionWidth() * 2,
                characters[i].keyframe.getRegionHeight() * 2
            );

            Assets.drawString(
                batch,
                characters[i].name,
                p.x + Assets.hudPatch.getPadLeft(),
                p.y + Assets.hudPatch.getPadBottom() + 20,
                Color.WHITE,
                .4f
            );

            Assets.drawString(
                batch,
                "Speed",
                p.x + Assets.hudPatch.getPadLeft(),
                p.y + Assets.hudPatch.getPadBottom() + 120,
                Color.WHITE,
                .4f
            );

            batch.setColor(Color.BLACK);
            batch.draw(
                Assets.white,
                p.x + Assets.hudPatch.getPadLeft(),
                p.y + Assets.hudPatch.getPadBottom() + 80,
                100,
                15
            );
            batch.setColor(Color.RED);
            batch.draw(
                Assets.white,
                p.x + Assets.hudPatch.getPadLeft(),
                p.y + Assets.hudPatch.getPadBottom() + 80,
                (characters[i].moveSpeed / PlayerCharacter.maxMoveSpeed) * 100,
                15
            );
            if (i != hoverCharacter) {
                batch.setColor(new Color(0, 0, 0, .5f));
                batch.draw(Assets.white, p.x, p.y, p.width, p.height);
            }
            batch.setColor(Color.WHITE);
        }

        batch.end();
    }
}
