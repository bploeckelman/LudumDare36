package lando.systems.ld36.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld36.utils.accessors.*;
import lando.systems.ld36.utils.particles.ParticleManager;


public class Assets {

    public static AssetManager mgr;
    public static TweenManager tween;
    public static ParticleManager particles;

    public static SpriteBatch batch;
    public static ShapeRenderer shapes;

    public static KeyMapping keyMapping;

    public static GlyphLayout glyphLayout;
    public static BitmapFont font;

    public static ShaderProgram fontShader;
    public static ShaderProgram crtShader;

    public static TextureAtlas atlas;

    public static boolean initialized;
    public static Texture debugTexture;
    public static Texture shadowTexture;

    public static Animation floppyWalk;
    public static Animation floppyPunch;

    public static Animation flashWalk;
    public static Animation flashPunch;

    public static Animation eightTrackWalk;
    public static Animation eightTrackPunch;

    public static Animation betamaxWalk;
    public static Animation betamaxPunch;

    public static Animation smartPhone;
    public static Animation smartPhoneKnife;

    public static NinePatch hudPatch;

    public static TextureRegion white;


    public static void load() {
        initialized = false;

        if (tween == null) {
            tween = new TweenManager();
            Tween.setCombinedAttributesLimit(4);
            Tween.registerAccessor(Color.class, new ColorAccessor());
            Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
            Tween.registerAccessor(Vector2.class, new Vector2Accessor());
            Tween.registerAccessor(Vector3.class, new Vector3Accessor());
            Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());
        }

        glyphLayout = new GlyphLayout();
//        font = new BitmapFont();
//        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        font.getData().setScale(2f);
//        font.getData().markupEnabled = true;

        final TextureLoader.TextureParameter linearParams = new TextureLoader.TextureParameter();
        linearParams.minFilter = Texture.TextureFilter.Linear;
        linearParams.magFilter = Texture.TextureFilter.Linear;

        final TextureLoader.TextureParameter nearestParams = new TextureLoader.TextureParameter();
        nearestParams.minFilter = Texture.TextureFilter.Nearest;
        nearestParams.magFilter = Texture.TextureFilter.Nearest;

        mgr = new AssetManager();
        mgr.load("images/spritesheet.png", Texture.class, nearestParams);
        mgr.load("images/shadow.png", Texture.class, linearParams);
        mgr.load("images/title-screen.png", Texture.class, linearParams);
        mgr.load("images/pcb-full.png", Texture.class, nearestParams);

        atlas = new TextureAtlas(Gdx.files.internal("sprites.atlas"));


        keyMapping = new KeyMapping();
    }

    public static float update() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1f;
        initialized = true;

        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        particles = new ParticleManager();

        Texture distText = new Texture(Gdx.files.internal("fonts/ubuntu.png"), true);
        distText.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);

        font = new BitmapFont(Gdx.files.internal("fonts/ubuntu.fnt"), new TextureRegion(distText), false);
        fontShader = new ShaderProgram(Gdx.files.internal("shaders/dist.vert"),
                                       Gdx.files.internal("shaders/dist.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }

        crtShader = compileShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/crt.frag"));

        debugTexture = mgr.get("images/spritesheet.png", Texture.class);
        shadowTexture = mgr.get("images/shadow.png", Texture.class);

        floppyWalk = new Animation(.15f, atlas.findRegions("Floppy_Walk"));
        floppyWalk.setPlayMode(Animation.PlayMode.LOOP);
        Array<TextureAtlas.AtlasRegion> floppyPunchTextures = atlas.findRegions("Floppy_Punch");
        floppyPunchTextures.add(floppyPunchTextures.get(1));
        floppyPunchTextures.add(floppyPunchTextures.get(0));
        floppyPunch = new Animation(.05f, floppyPunchTextures);


        flashWalk = new Animation(.15f, atlas.findRegions("Flash_Walk"));
        flashWalk.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> flashPunchTextures = atlas.findRegions("Flash_Punch");
        flashPunchTextures.add(flashPunchTextures.get(2));
        flashPunchTextures.add(flashPunchTextures.get(1));
        flashPunchTextures.add(flashPunchTextures.get(0));
        flashPunch = new Animation(.05f, flashPunchTextures);

        eightTrackWalk = new Animation(.15f, atlas.findRegions("Eight_Track_Walk"));
        eightTrackWalk.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> eightTrackPunchTextures = atlas.findRegions("Eight_Track_Punch");
        eightTrackPunch = new Animation(.08f, eightTrackPunchTextures);


        betamaxWalk = new Animation(.15f, atlas.findRegions("Betamax_Walk"));
        betamaxWalk.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> betamaxPunchTextures = atlas.findRegions("Betamax_Punch");
        betamaxPunchTextures.add(betamaxPunchTextures.get(2));
        betamaxPunchTextures.add(betamaxPunchTextures.get(1));
        betamaxPunchTextures.add(betamaxPunchTextures.get(0));
        betamaxPunch = new Animation(.03f, betamaxPunchTextures);


        smartPhone = new Animation(.15f, atlas.findRegions("Smart_Phone_Walk"));
        smartPhone.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> smartPhoneKnifeTextures = atlas.findRegions("Smart_Phone_Knife");
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(4));
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(3));
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(2));
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(1));
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(0));
        smartPhoneKnife = new Animation(.01f, smartPhoneKnifeTextures);

        hudPatch = new NinePatch(atlas.findRegion("ninepatch-hud"), 24, 24, 24, 24);

        white = atlas.findRegion("white");

        return 1f;
    }

    public static void dispose() {
        batch.dispose();
        shapes.dispose();
        font.dispose();
        mgr.clear();
    }

    private static ShaderProgram compileShaderProgram(FileHandle vertSource, FileHandle fragSource) {
//        ShaderProgram.pedantic = false;
        final ShaderProgram shader = new ShaderProgram(vertSource, fragSource);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Failed to compile shader program:\n" + shader.getLog());
        }
        else if (shader.getLog().length() > 0) {
            Gdx.app.debug("SHADER", "ShaderProgram compilation log:\n" + shader.getLog());
        }
        return shader;
    }

    public static void drawString(SpriteBatch batch, String text, float x, float y, Color c, float scale){
        batch.setShader(Assets.fontShader);
        Assets.fontShader.setUniformf("u_scale", scale);
        font.getData().setScale(scale);
        font.setColor(c);
        font.draw(batch, text, x, y);
        font.getData().setScale(1f);
        batch.setShader(null);
    }

}
