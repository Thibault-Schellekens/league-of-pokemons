package be.esi.prj.leagueofpokemons.util;

import javafx.scene.media.AudioClip;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static AudioManager instance;
    private final Map<AudioSound, AudioClip> soundMap;

    private final SettingsManager settingsManager;

    private AudioManager() {
        soundMap = new HashMap<>();
        settingsManager = SettingsManager.getInstance();
        initSounds();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playSound(AudioSound sound) {
        AudioClip clip = soundMap.get(sound);
        clip.play((double) settingsManager.getVolume() / 100);
    }

    /**
     * Playing a sound for the first time has some latency.
     * We play each sound when initializing the AudioManager, at volume 0 to load them,
     * avoiding any latency when the real sound has to be played.
     */
    private void initSounds() {
        for (AudioSound sound : AudioSound.values()) {
            AudioClip clip = new AudioClip(getClass().getResource(sound.getFilePath()).toString());
            soundMap.put(sound, clip);
            clip.play(0);
        }
    }
}
