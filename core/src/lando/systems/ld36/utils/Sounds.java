package lando.systems.ld36.utils;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class Sounds {

    public enum Effect {
        playerSwitch,
        playerSelect,
    }

    private static HashMap<Effect, Sound> soundMap = new HashMap<Effect, Sound>();

    public static Music gameMusic;
    public static MutableFloat musicVolume;

    public static void load() {
        soundMap.put(Effect.playerSwitch, Gdx.audio.newSound(Gdx.files.internal("sounds/switching_menu.mp3")));
        soundMap.put(Effect.playerSelect, Gdx.audio.newSound(Gdx.files.internal("sounds/select_tone.mp3")));

        if (gameMusic == null) {
            musicVolume = new MutableFloat(0);
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
            gameMusic.setLooping(true);
            gameMusic.play();
            gameMusic.setVolume(0.3f);
        }
    }

    public static void dispose() {
        Effect[] allSounds = Effect.values();
        for (Effect allSound : allSounds) {
            soundMap.get(allSound).dispose();
        }
    }

    public static long play(Effect soundEffect) {
        return soundMap.get(soundEffect).play(0.3f);
    }

    public static void stop(Effect soundEffect) {
        Sound sound = soundMap.get(soundEffect);
        if (sound != null) {
            sound.stop();
        }
    }

}
