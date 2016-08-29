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

    public static BitmapFont emuLogicFont;

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
    public static Animation flashWalkMedium;
    public static Animation flashPunchMedium;
    public static Animation flashWalkHard;
    public static Animation flashPunchHard;

    public static Animation eightTrackWalk;
    public static Animation eightTrackPunch;

    public static Animation betamaxWalk;
    public static Animation betamaxPunch;

    public static Animation sdWalk;
    public static Animation sdAttack;
    public static Animation sdWalkMedium;
    public static Animation sdAttackMedium;
    public static Animation sdWalkHard;
    public static Animation sdAttackHard;

    public static Animation smartPhone;
    public static Animation smartPhoneKnife;
    public static Animation smartPhoneMedium;
    public static Animation smartPhoneKnifeMedium;
    public static Animation smartPhoneHard;
    public static Animation smartPhoneKnifeHard;

    public static Animation cloud;

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

        Texture emuFontTexture = new Texture(Gdx.files.internal("fonts/emulogic-16pt.png"), true);
        emuFontTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        emuLogicFont = new BitmapFont(Gdx.files.internal("fonts/emulogic-16pt.fnt"),
            new TextureRegion(emuFontTexture));

        crtShader = compileShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/crt.frag"));

        debugTexture = mgr.get("images/spritesheet.png", Texture.class);
        shadowTexture = mgr.get("images/shadow.png", Texture.class);

        floppyWalk = new Animation(.15f, atlas.findRegions("Floppy_Walk"));
        floppyWalk.setPlayMode(Animation.PlayMode.LOOP);
        Array<TextureAtlas.AtlasRegion> floppyPunchTextures = atlas.findRegions("Floppy_Punch");
        floppyPunchTextures.add(floppyPunchTextures.get(1));
        floppyPunchTextures.add(floppyPunchTextures.get(0));
        floppyPunch = new Animation(.05f, floppyPunchTextures);


        Array<TextureAtlas.AtlasRegion> flashPunchTextures = atlas.findRegions("Flash_Punch");
        flashPunchTextures.add(flashPunchTextures.get(2));
        flashPunchTextures.add(flashPunchTextures.get(1));
        flashPunchTextures.add(flashPunchTextures.get(0));
        flashPunch = new Animation(.05f, flashPunchTextures);

        flashWalk = new Animation(.15f, atlas.findRegions("Flash_Walk"));
        flashWalk.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> flashPunchMediumTextures = atlas.findRegions("Flash_Punch_Medium");
        flashPunchMediumTextures.add(flashPunchMediumTextures.get(2));
        flashPunchMediumTextures.add(flashPunchMediumTextures.get(1));
        flashPunchMediumTextures.add(flashPunchMediumTextures.get(0));
        flashPunchMedium = new Animation(.05f, flashPunchMediumTextures);

        flashWalkMedium = new Animation(.15f, atlas.findRegions("Flash_Walk_Medium"));
        flashWalkMedium.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> flashPunchHardTextures = atlas.findRegions("Flash_Punch_Hard");
        flashPunchHardTextures.add(flashPunchHardTextures.get(2));
        flashPunchHardTextures.add(flashPunchHardTextures.get(1));
        flashPunchHardTextures.add(flashPunchHardTextures.get(0));
        flashPunchHard = new Animation(.05f, flashPunchHardTextures);

        flashWalkHard = new Animation(.15f, atlas.findRegions("Flash_Walk_Hard"));
        flashWalkHard.setPlayMode(Animation.PlayMode.LOOP);

        eightTrackWalk = new Animation(.15f, atlas.findRegions("Eight_Track_Walk"));
        eightTrackWalk.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> eightTrackPunchTextures = atlas.findRegions("Eight_Track_Punch");
        eightTrackPunchTextures.add(eightTrackPunchTextures.get(1));
        eightTrackPunchTextures.add(eightTrackPunchTextures.get(0));
        eightTrackPunch = new Animation(.15f, eightTrackPunchTextures);


        betamaxWalk = new Animation(.15f, atlas.findRegions("Betamax_Walk"));
        betamaxWalk.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> betamaxPunchTextures = atlas.findRegions("Betamax_Punch");
        betamaxPunchTextures.add(betamaxPunchTextures.get(2));
        betamaxPunchTextures.add(betamaxPunchTextures.get(1));
        betamaxPunchTextures.add(betamaxPunchTextures.get(0));
        betamaxPunch = new Animation(.03f, betamaxPunchTextures);


        sdWalk = new Animation(.15f, atlas.findRegions("SD_Walk"));
        sdWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Array<TextureAtlas.AtlasRegion> sdAttackTextures = atlas.findRegions("SD_Attack");
        sdAttackTextures.add(sdAttackTextures.get(2));
        sdAttackTextures.add(sdAttackTextures.get(1));
        sdAttackTextures.add(sdAttackTextures.get(0));
        sdAttack = new Animation(.05f, sdAttackTextures);

        sdWalkMedium = new Animation(.15f, atlas.findRegions("SD_Walk_Medium"));
        sdWalkMedium.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Array<TextureAtlas.AtlasRegion> sdAttackTexturesMedium = atlas.findRegions("SD_Attack_Medium");
        sdAttackTexturesMedium.add(sdAttackTexturesMedium.get(2));
        sdAttackTexturesMedium.add(sdAttackTexturesMedium.get(1));
        sdAttackTexturesMedium.add(sdAttackTexturesMedium.get(0));
        sdAttackMedium = new Animation(.05f, sdAttackTexturesMedium);

        sdWalkHard = new Animation(.15f, atlas.findRegions("SD_Walk_Hard"));
        sdWalkHard.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Array<TextureAtlas.AtlasRegion> sdAttackTexturesHard = atlas.findRegions("SD_Attack_Hard");
        sdAttackTexturesHard.add(sdAttackTexturesHard.get(2));
        sdAttackTexturesHard.add(sdAttackTexturesHard.get(1));
        sdAttackTexturesHard.add(sdAttackTexturesHard.get(0));
        sdAttackHard = new Animation(.05f, sdAttackTexturesHard);

        smartPhone = new Animation(.15f, atlas.findRegions("Smart_Phone_Walk"));
        smartPhone.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> smartPhoneKnifeTextures = atlas.findRegions("Smart_Phone_Knife");
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(4));
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(3));
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(2));
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(1));
        smartPhoneKnifeTextures.add(smartPhoneKnifeTextures.get(0));
        smartPhoneKnife = new Animation(.03f, smartPhoneKnifeTextures);

        smartPhoneMedium = new Animation(.15f, atlas.findRegions("Smart_Phone_Walk_Medium"));
        smartPhoneMedium.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> smartPhoneKnifeTexturesMedium = atlas.findRegions("Smart_Phone_Knife_Medium");
        smartPhoneKnifeTexturesMedium.add(smartPhoneKnifeTexturesMedium.get(4));
        smartPhoneKnifeTexturesMedium.add(smartPhoneKnifeTexturesMedium.get(3));
        smartPhoneKnifeTexturesMedium.add(smartPhoneKnifeTexturesMedium.get(2));
        smartPhoneKnifeTexturesMedium.add(smartPhoneKnifeTexturesMedium.get(1));
        smartPhoneKnifeTexturesMedium.add(smartPhoneKnifeTexturesMedium.get(0));
        smartPhoneKnifeMedium = new Animation(.01f, smartPhoneKnifeTexturesMedium);

        smartPhoneHard = new Animation(.15f, atlas.findRegions("Smart_Phone_Walk_Hard"));
        smartPhoneHard.setPlayMode(Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> smartPhoneKnifeTexturesHard = atlas.findRegions("Smart_Phone_Knife_Hard");
        smartPhoneKnifeTexturesHard.add(smartPhoneKnifeTexturesHard.get(4));
        smartPhoneKnifeTexturesHard.add(smartPhoneKnifeTexturesHard.get(3));
        smartPhoneKnifeTexturesHard.add(smartPhoneKnifeTexturesHard.get(2));
        smartPhoneKnifeTexturesHard.add(smartPhoneKnifeTexturesHard.get(1));
        smartPhoneKnifeTexturesHard.add(smartPhoneKnifeTexturesHard.get(0));
        smartPhoneKnifeHard = new Animation(.01f, smartPhoneKnifeTexturesHard);

        cloud = new Animation(.1f, atlas.findRegions("The_Cloud"));
        cloud.setPlayMode(Animation.PlayMode.LOOP);

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
