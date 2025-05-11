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

/**
 * Singleton class responsible for managing and playing audio in the application.
 * Supports both {@link AudioClip} for short sounds and {@link MediaPlayer} for longer sounds or background music.
 * Handles the playback of sounds, volume adjustments, and the lifecycle of media resources.
 */
public class AudioManager {
    private static AudioManager instance;

    private final Map<AudioSound, AudioClip> soundMap;
    private final List<AudioSound> clipsPlaying;

    private final Map<AudioSound, Media> mediaMap;
    private final Map<AudioSound, MediaPlayer> activeMediaPlayers;
    private final Map<AudioSound, Double> mediaVolumeMultipliers;

    private final Set<AudioSound> useMediaPlayerSounds;

    private final SettingsManager settingsManager;

    /**
     * Private constructor to initialize audio resources and media players.
     * Sets up sound maps, media player settings, and the settings manager.
     */
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

    /**
     * Initializes the media player sounds that require {@link MediaPlayer} for playback.
     */
    private void initMediaPlayerSounds() {
        useMediaPlayerSounds.add(AudioSound.IN_BATTLE);
    }

    /**
     * Returns the singleton instance of the AudioManager.
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of {@link AudioManager}.
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Plays the specified sound at the default volume (1.0).
     *
     * @param sound the {@link AudioSound} to be played.
     */
    public void playSound(AudioSound sound) {
        playSound(sound, 1.0);
    }

    /**
     * Plays the specified sound at the given volume.
     *
     * @param sound the {@link AudioSound} to be played.
     * @param volume the volume multiplier for the sound (ranges from 0.0 to 1.0).
     */
    public void playSound(AudioSound sound, double volume) {
        if (useMediaPlayerSounds.contains(sound)) {
            playWithMediaPlayer(sound, volume);
        } else {
            playWithAudioClip(sound, volume);
        }
    }

    /**
     * Plays the sound using an {@link AudioClip} with the specified volume.
     *
     * @param sound the {@link AudioSound} to be played.
     * @param volume the volume multiplier for the sound (ranges from 0.0 to 1.0).
     */
    private void playWithAudioClip(AudioSound sound, double volume) {
        AudioClip clip = soundMap.get(sound);
        double actualVolume = ((double) settingsManager.getVolume() / 100) * volume;
        clip.play(actualVolume);

        clipsPlaying.add(sound);
    }

    /**
     * Plays the sound using a {@link MediaPlayer} with the specified volume.
     *
     * @param sound the {@link AudioSound} to be played.
     * @param volume the volume multiplier for the sound (ranges from 0.0 to 1.0).
     */
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

    /**
     * Stops the specified sound. If the sound is using a {@link MediaPlayer}, it stops and disposes of it.
     * If the sound is using an {@link AudioClip}, it stops the clip playback.
     *
     * @param sound the {@link AudioSound} to stop.
     */
    public void stopSound(AudioSound sound) {
        if (useMediaPlayerSounds.contains(sound)) {
            stopMediaPlayer(sound);
        } else {
            stopAudioClip(sound);
        }
    }

    /**
     * Stops the playback of the specified {@link AudioClip} sound.
     *
     * @param sound the {@link AudioSound} whose clip to stop.
     */
    private void stopAudioClip(AudioSound sound) {
        AudioClip clip = soundMap.get(sound);
        clip.stop();
        clipsPlaying.remove(sound);
    }

    /**
     * Stops the playback and disposes of the specified {@link MediaPlayer} sound.
     *
     * @param sound the {@link AudioSound} whose media player to stop.
     */
    private void stopMediaPlayer(AudioSound sound) {
        if (activeMediaPlayers.containsKey(sound)) {
            MediaPlayer player = activeMediaPlayers.get(sound);
            player.stop();
            player.dispose();
            activeMediaPlayers.remove(sound);
            mediaVolumeMultipliers.remove(sound);
        }
    }

    /**
     * Updates the volume for all currently playing media players based on the settings.
     */
    public void updateAllSoundVolumes() {
        for (Map.Entry<AudioSound, MediaPlayer> entry : activeMediaPlayers.entrySet()) {
            AudioSound sound = entry.getKey();
            MediaPlayer player = entry.getValue();

            double volumeMultiplier = mediaVolumeMultipliers.getOrDefault(sound, 1.0);
            double actualVolume = ((double) settingsManager.getVolume() / 100) * volumeMultiplier;
            player.setVolume(actualVolume);
        }
    }

    /**
     * Initializes all sounds, preloading the resources for {@link AudioClip} and {@link Media}.
     */
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

    /**
     * Shuts down the audio manager, stopping and disposing all active audio resources.
     */
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
