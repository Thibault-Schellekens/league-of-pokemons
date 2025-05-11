package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.util.audio.AudioManager;

/**
 * Singleton class for managing application settings, including volume control,
 * animation preferences, and animation speed.
 * <p>
 * The {@link SettingsManager} ensures that the settings are consistent throughout
 * the application and provides methods to update and retrieve the settings.
 */
public class SettingsManager {
    private static SettingsManager instance;

    private int volume;
    private boolean skipAnimation;
    private double animationSpeed;

    private final double maxAnimationSpeed;

    /**
     * Private constructor to initialize default settings.
     */
    private SettingsManager() {
        volume = 100;
        skipAnimation = true;
        animationSpeed = 1.0;
        maxAnimationSpeed = 3.0;
    }

    /**
     * Gets the singleton instance of the {@link SettingsManager}.
     *
     * @return the singleton instance of the {@link SettingsManager}
     */
    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    /**
     * Gets the current volume setting.
     *
     * @return the current volume as a percentage (0 to 100)
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Sets the volume level and updates the {@link AudioManager}.
     *
     * @param volume the new volume level (0 to 100)
     */
    public void setVolume(int volume) {
        int oldVolume = this.volume;
        this.volume = volume;

        if (oldVolume != volume) {
            // Keep the AudioManager.getInstance() here, and not in the constructor
            // Or else it'll create an infinite recursive loop.
            AudioManager.getInstance().updateAllSoundVolumes();
        }
    }

    /**
     * Determines whether the animation should be skipped.
     *
     * @return true if animations should be skipped, false otherwise
     */
    public boolean isSkipAnimation() {
        return skipAnimation;
    }

    /**
     * Sets the flag to indicate whether animations should be skipped.
     *
     * @param skipAnimation true to skip animations, false to show animations
     */
    public void setSkipAnimation(boolean skipAnimation) {
        this.skipAnimation = skipAnimation;
    }

    /**
     * Gets the current animation speed.
     *
     * @return the animation speed multiplier (1.0 is normal speed)
     */
    public double getAnimationSpeed() {
        return animationSpeed;
    }

    /**
     * Sets the animation speed multiplier.
     *
     * @param animationSpeed the new animation speed multiplier
     */
    public void setAnimationSpeed(double animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    /**
     * Gets the maximum allowed animation speed.
     *
     * @return the maximum animation speed
     */
    public double getMaxAnimationSpeed() {
        return maxAnimationSpeed;
    }
}
