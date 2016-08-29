package lando.systems.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld36.LudumDare36;
import lando.systems.ld36.entities.Player;
import lando.systems.ld36.entities.PlayerCharacter;
import lando.systems.ld36.utils.Assets;
import lando.systems.ld36.utils.Sounds;
import lando.systems.ld36.utils.Statistics;

public class CharacterSelectScreen extends BaseScreen {
    float border = 4f;
    Rectangle topPanel;
    Array<Rectangle> characterPanels;
    float timer = 0f;
    float bouncer = 0f;
    int hoverCharacter;
    Vector3 cursorPoint;
    Vector3 lastCursorPoint;

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
        cursorPoint = new Vector3();
        lastCursorPoint = new Vector3();
        hoverCharacter = 0;
    }

    @Override
    public void update(float dt) {
        timer += dt;
        bouncer += 500f * dt;
        if (bouncer > 360f) bouncer -= 360f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare36.game.setScreen( new MenuScreen());
        }

        boolean playerSelectChanged = false;

        PlayerCharacter[] characters = PlayerCharacter.values();
        cursorPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        hudCamera.unproject(cursorPoint);

        if (!cursorPoint.epsilonEquals(lastCursorPoint, .5f)) {
            for (int i = 0; i < characterPanels.size; i++) {
                if (characterPanels.get(i).contains(cursorPoint.x, cursorPoint.y)) {
                    if (hoverCharacter != i) {
                        playerSelectChanged = true;
                    }
                    hoverCharacter = i;
                    break;
                }
            }
        }

        lastCursorPoint.set(cursorPoint);

        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            hoverCharacter--;
            if (hoverCharacter<0){
                hoverCharacter += characterPanels.size;
            }
            playerSelectChanged = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            hoverCharacter++;
            if (hoverCharacter >= characterPanels.size){
                hoverCharacter -= characterPanels.size;
            }
            playerSelectChanged = true;
        }

        if (playerSelectChanged) {
            Sounds.play(Sounds.Effect.playerSwitch);
        }

        if (hoverCharacter >= 0) {
            characters[hoverCharacter].keyframe = characters[hoverCharacter].walkAnimation.getKeyFrame(timer);
            if ((Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER))) {
                LudumDare36.game.setScreen(new GameScreen(characters[hoverCharacter]));
                Sounds.play(Sounds.Effect.playerSelect);
            }
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

            for (int c = 0; c < characters[i].name.length(); ++c) {
                Assets.drawString(
                    batch,
                    "" + characters[i].name.charAt(c),
                    p.x + Assets.hudPatch.getPadLeft() + (Assets.font.getSpaceWidth() * 0.279f * c),
                    p.y + Assets.hudPatch.getPadBottom() + 25
                        + ((i != hoverCharacter) ? 0 : MathUtils.sinDeg(bouncer + 20f*(c+1)) * 5f - 2.5f),
                    (i != hoverCharacter) ? Color.WHITE : Color.YELLOW,
                    .4f
                );
            }

            Assets.drawString(
                batch,
                "Speed",
                p.x + Assets.hudPatch.getPadLeft(),
                p.y + Assets.hudPatch.getPadBottom() + 130,
                Color.WHITE,
                .4f
            );

            batch.setColor(Color.BLACK);
            batch.draw(
                Assets.white,
                p.x + Assets.hudPatch.getPadLeft(),
                p.y + Assets.hudPatch.getPadBottom() + 90,
                100,
                15
            );
            batch.setColor(Color.RED);
            batch.draw(
                Assets.white,
                p.x + Assets.hudPatch.getPadLeft(),
                p.y + Assets.hudPatch.getPadBottom() + 90,
                (characters[i].moveSpeed / PlayerCharacter.maxMoveSpeed) * 100,
                15
            );

            Assets.drawString(
                batch,
                "Attack",
                p.x + Assets.hudPatch.getPadLeft(),
                p.y + Assets.hudPatch.getPadBottom() + 80,
                Color.WHITE,
                .4f
            );

            batch.setColor(Color.BLACK);
            batch.draw(
                    Assets.white,
                    p.x + Assets.hudPatch.getPadLeft(),
                    p.y + Assets.hudPatch.getPadBottom() + 40,
                    100,
                    15
            );
            batch.setColor(Color.RED);
            batch.draw(
                    Assets.white,
                    p.x + Assets.hudPatch.getPadLeft(),
                    p.y + Assets.hudPatch.getPadBottom() + 40,
                    (characters[i].attackPower/ PlayerCharacter.maxAttackPower) * 100,
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
