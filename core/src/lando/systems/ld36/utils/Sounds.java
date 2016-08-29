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
        playerDeath,
        playerHitEnemy,
        playerJump,
        playerMissedPunch,
        playerHurt,
        enemyDead,
        levelEnd,
        cloudSpawn1,
        cloudSpawn2,
        bossSpawn,
        bossDead,
        cloudDead,
    }

    private static HashMap<Effect, Sound> soundMap = new HashMap<Effect, Sound>();

    public static Music gameMusic;
    public static MutableFloat musicVolume;

    public static void load() {
        soundMap.put(Effect.playerSwitch,      Gdx.audio.newSound(Gdx.files.internal("sounds/switching_menu.mp3")));
        soundMap.put(Effect.playerSelect,      Gdx.audio.newSound(Gdx.files.internal("sounds/select_tone.mp3")));
        soundMap.put(Effect.playerDeath,       Gdx.audio.newSound(Gdx.files.internal("sounds/oh_no.mp3")));
        soundMap.put(Effect.playerMissedPunch, Gdx.audio.newSound(Gdx.files.internal("sounds/missed_punch.mp3")));
        soundMap.put(Effect.playerHitEnemy,    Gdx.audio.newSound(Gdx.files.internal("sounds/punching_enemy_sound.mp3")));
        soundMap.put(Effect.playerJump,        Gdx.audio.newSound(Gdx.files.internal("sounds/landing_character.mp3")));
        soundMap.put(Effect.playerHurt,        Gdx.audio.newSound(Gdx.files.internal("sounds/punching_a_solid_object.mp3")));
        soundMap.put(Effect.enemyDead,         Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_death.mp3")));
        soundMap.put(Effect.levelEnd,          Gdx.audio.newSound(Gdx.files.internal("sounds/item_pickup.mp3")));
        soundMap.put(Effect.bossSpawn,         Gdx.audio.newSound(Gdx.files.internal("sounds/whaaa.mp3")));
        soundMap.put(Effect.bossDead,          Gdx.audio.newSound(Gdx.files.internal("sounds/breaking_glass.mp3")));
        soundMap.put(Effect.cloudSpawn1,       Gdx.audio.newSound(Gdx.files.internal("sounds/ominous.mp3")));
        soundMap.put(Effect.cloudSpawn2,       Gdx.audio.newSound(Gdx.files.internal("sounds/evil_laugh.mp3")));
        soundMap.put(Effect.cloudDead,         Gdx.audio.newSound(Gdx.files.internal("sounds/evil_laugh.mp3")));

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
        return soundMap.get(soundEffect).play(0.2f);
    }

    public static void stop(Effect soundEffect) {
        Sound sound = soundMap.get(soundEffect);
        if (sound != null) {
            sound.stop();
        }
    }

}
