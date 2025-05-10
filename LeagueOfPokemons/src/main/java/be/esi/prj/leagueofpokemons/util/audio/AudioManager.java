package be.esi.prj.leagueofpokemons.util.audio;

import be.esi.prj.leagueofpokemons.util.SettingsManager;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class AudioManager {
    private static AudioManager instance;

    private final Map<AudioSound, AudioClip> soundMap;
    private final List<AudioSound> clipsPlaying;

    private final Map<AudioSound, Media> mediaMap;
    private final Map<AudioSound, MediaPlayer> activeMediaPlayers;
    private final Map<AudioSound, Double> mediaVolumeMultipliers;

    private final Set<AudioSound> useMediaPlayerSounds;

    private final SettingsManager settingsManager;

    private AudioManager() {
        soundMap = new HashMap<>();
        clipsPlaying = new ArrayList<>();

        mediaMap = new HashMap<>();
        activeMediaPlayers = new HashMap<>();
        mediaVolumeMultipliers = new HashMap<>();

        useMediaPlayerSounds = new HashSet<>();
        initMediaPlayerSounds();

        settingsManager = SettingsManager.getInstance();
        initSounds();
    }

    private void initMediaPlayerSounds() {
        useMediaPlayerSounds.add(AudioSound.IN_BATTLE);
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playSound(AudioSound sound) {
        playSound(sound, 1.0);
    }

    public void playSound(AudioSound sound, double volume) {
        if (useMediaPlayerSounds.contains(sound)) {
            playWithMediaPlayer(sound, volume);
        } else {
            playWithAudioClip(sound, volume);
        }
    }

    private void playWithAudioClip(AudioSound sound, double volume) {
        AudioClip clip = soundMap.get(sound);
        double actualVolume = ((double) settingsManager.getVolume() / 100) * volume;
        clip.play(actualVolume);

        clipsPlaying.add(sound);
    }

    private void playWithMediaPlayer(AudioSound sound, double volume) {
        mediaVolumeMultipliers.put(sound, volume);

        Media media = mediaMap.get(sound);
        if (media == null) {
            return;
        }

        stopMediaPlayer(sound);

        MediaPlayer player = new MediaPlayer(media);
        double actualVolume = ((double) settingsManager.getVolume() / 100) * volume;
        player.setVolume(actualVolume);

        activeMediaPlayers.put(sound, player);
        player.play();
    }

    public void stopSound(AudioSound sound) {
        if (useMediaPlayerSounds.contains(sound)) {
            stopMediaPlayer(sound);
        } else {
            stopAudioClip(sound);
        }
    }

    private void stopAudioClip(AudioSound sound) {
        AudioClip clip = soundMap.get(sound);
        clip.stop();
        clipsPlaying.remove(sound);
    }

    private void stopMediaPlayer(AudioSound sound) {
        if (activeMediaPlayers.containsKey(sound)) {
            MediaPlayer player = activeMediaPlayers.get(sound);
            player.stop();
            player.dispose();
            activeMediaPlayers.remove(sound);
            mediaVolumeMultipliers.remove(sound);
        }
    }

    public void updateAllSoundVolumes() {
        for (Map.Entry<AudioSound, MediaPlayer> entry : activeMediaPlayers.entrySet()) {
            AudioSound sound = entry.getKey();
            MediaPlayer player = entry.getValue();

            double volumeMultiplier = mediaVolumeMultipliers.getOrDefault(sound, 1.0);
            double actualVolume = ((double) settingsManager.getVolume() / 100) * volumeMultiplier;
            player.setVolume(actualVolume);
        }
    }

    private void initSounds() {
        for (AudioSound sound : AudioSound.values()) {
            URL resourceUrl = getClass().getResource(sound.getFilePath());
            if (resourceUrl == null) {
                System.err.println("Could not find audio resource: " + sound.getFilePath());
                continue;
            }

            String resourcePath = resourceUrl.toString();

            if (useMediaPlayerSounds.contains(sound)) {
                Media media = new Media(resourcePath);
                mediaMap.put(sound, media);
            } else {
                AudioClip clip = new AudioClip(resourcePath);
                soundMap.put(sound, clip);
                clip.play(0); // Preload to avoid latency
            }
        }
    }

    public void shutdown() {
        for (MediaPlayer player : activeMediaPlayers.values()) {
            player.stop();
            player.dispose();
        }
        activeMediaPlayers.clear();

        for (AudioSound sound : clipsPlaying) {
            AudioClip clip = soundMap.get(sound);
            clip.stop();
        }
        clipsPlaying.clear();
    }

}