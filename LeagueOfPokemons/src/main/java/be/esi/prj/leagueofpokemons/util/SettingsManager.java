package be.esi.prj.leagueofpokemons.util;

public class SettingsManager {
    private static SettingsManager instance;

    private double volume;
    private boolean skipAnimation;
    private double animationSpeed;

    private SettingsManager() {
        volume = 1.0;
        skipAnimation = false;
        animationSpeed = 1.0;
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
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
}
