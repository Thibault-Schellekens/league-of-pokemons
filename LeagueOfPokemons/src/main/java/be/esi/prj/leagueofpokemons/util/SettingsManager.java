package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.util.audio.AudioManager;

//todo image for settings
public class SettingsManager {
    private static SettingsManager instance;

    private int volume;
    private boolean skipAnimation;
    private double animationSpeed;

    private final double maxAnimationSpeed;

    private SettingsManager() {
        volume = 100;
        skipAnimation = true;
        animationSpeed = 1.0;
        maxAnimationSpeed = 3.0;
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        int oldVolume = this.volume;
        this.volume = volume;

        if (oldVolume != volume) {
            // Keep the AudioManager.getInstance() here, and not in the constructor
            // Or else it'll create an infinite recursive loop.
            AudioManager.getInstance().updateAllSoundVolumes();
        }
    }

    public boolean isSkipAnimation() {
        return skipAnimation;
    }

    public void setSkipAnimation(boolean skipAnimation) {
        this.skipAnimation = skipAnimation;
    }

    public double getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(double animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public double getMaxAnimationSpeed() {
        return maxAnimationSpeed;
    }
}
