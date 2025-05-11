package be.esi.prj.leagueofpokemons.animation;

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility class responsible for handling battle animations such as taking damage,
 * healing, and fainting. Animations are queued to ensure smooth and sequential playback.
 */
public class BattleAnimation {

    private BattleAnimation() {
    }

    private static final AtomicBoolean isAnimationPlaying = new AtomicBoolean(false);
    private static final Queue<Animation> animationQueue = new LinkedList<>();
    private static Runnable updateFunction;

    /**
     * Sets the function to execute after the death animation completes.
     *
     * @param function a {@link Runnable} to execute after animation
     */
    public static void setFunction(Runnable function) {
        updateFunction = function;
    }

    /**
     * Returns whether an animation is currently playing.
     *
     * @return true if an animation is in progress, false otherwise
     */
    public static boolean isAnimationPlaying() {
        return isAnimationPlaying.get();
    }

    /**
     * Queues and plays the damage animation for a Pokémon, including shake, flash,
     * health bar decrease, and HP text countdown.
     *
     * @param pokemonImage   the {@link ImageView} of the Pokémon
     * @param pokemonHPBar   the {@link ProgressBar} showing health
     * @param newBarValue    the new health bar value (0.0–1.0)
     * @param pokemonHPText  the {@link Text} displaying HP
     * @param newHP          the new HP value
     */
    public static void playDamageAnimation(ImageView pokemonImage, ProgressBar pokemonHPBar, double newBarValue, Text pokemonHPText, int newHP) {
        int currentHP = Integer.parseInt(pokemonHPText.getText());

        Timeline shakeAnimation = createShakeAnimation(pokemonImage);
        Timeline flashAnimation = createFlashAnimation(pokemonImage);
        Timeline healthBarValueAnimation = createAnimateHealthBarValue(pokemonHPBar, newBarValue);
        Timeline HPTextAnimation = createAnimateHPText(pokemonHPText, currentHP, newHP);

        ParallelTransition damageAnimations = new ParallelTransition(shakeAnimation, flashAnimation, healthBarValueAnimation, HPTextAnimation);
        queueAnimation(damageAnimations);
    }

    /**
     * Queues and plays the healing animation for a Pokémon, including health bar increase
     * and HP text count-up.
     *
     * @param pokemonHPBar  the {@link ProgressBar} showing health
     * @param newBarValue   the new health bar value (0.0–1.0)
     * @param pokemonHPText the {@link Text} displaying HP
     * @param newHP         the new HP value
     */
    public static void playRestoreHealthAnimation(ProgressBar pokemonHPBar, double newBarValue, Text pokemonHPText, int newHP) {
        int currentHP = Integer.parseInt(pokemonHPText.getText());

        Timeline healthBarValueAnimation = createAnimateHealthBarValue(pokemonHPBar, newBarValue);
        Timeline HPTextAnimation = createAnimateHPText(pokemonHPText, currentHP, newHP);

        ParallelTransition restoreAnimations = new ParallelTransition(healthBarValueAnimation, HPTextAnimation);
        queueAnimation(restoreAnimations);
    }

    /**
     * Queues and plays the fainting (death) animation for a Pokémon, which includes
     * fading, dropping, and rotating.
     *
     * @param pokemonImage the {@link ImageView} of the Pokémon
     */
    public static void playDeathAnimation(ImageView pokemonImage) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2.5), pokemonImage);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition dropDown = new TranslateTransition(Duration.seconds(2.2), pokemonImage);
        dropDown.setByY(30);
        dropDown.setInterpolator(Interpolator.EASE_IN);

        RotateTransition tiltRotation = new RotateTransition(Duration.seconds(2.2), pokemonImage);
        tiltRotation.setByAngle(15);
        tiltRotation.setInterpolator(Interpolator.EASE_IN);

        ParallelTransition fallingAnimation = new ParallelTransition(fadeOut, dropDown, tiltRotation);
        SequentialTransition deathSequence = new SequentialTransition(fallingAnimation);

        deathSequence.setOnFinished(e -> {
            if (updateFunction != null) {
                updateFunction.run();
                updateFunction = null;
                pokemonImage.setOpacity(1.0);
            }
            pokemonImage.setTranslateY(0);
            pokemonImage.setRotate(0);
        });

        queueAnimation(deathSequence);
    }

    /**
     * Adds an animation to the queue and attempts to play it if no other animation is running.
     *
     * @param animation the {@link Animation} to be queued
     */
    private static void queueAnimation(Animation animation) {
        animationQueue.add(animation);
        playNextAnimation();
    }

    /**
     * Plays the next animation in the queue, if any, and sets up a handler
     * to continue with the next animation after the current one finishes.
     */
    private static void playNextAnimation() {
        if (!isAnimationPlaying.get() && !animationQueue.isEmpty()) {
            Animation next = animationQueue.poll();
            if (next != null) {
                isAnimationPlaying.set(true);
                EventHandler<ActionEvent> existingHandler = next.getOnFinished();
                next.setOnFinished(e -> {
                    if (existingHandler != null) {
                        existingHandler.handle(e);
                    }
                    isAnimationPlaying.set(false);
                    playNextAnimation();
                });

                next.play();
            }
        }
    }

    /**
     * Creates a shaking animation to simulate a Pokémon taking damage.
     *
     * @param pokemonImage the {@link ImageView} of the Pokémon
     * @return a {@link Timeline} representing the shake
     */
    private static Timeline createShakeAnimation(ImageView pokemonImage) {
        Timeline shakeTimeline = new Timeline();

        double shakeIntensity = 10.0;
        int shakeCount = 6;
        Duration shakeDuration = Duration.millis(75);

        for (int i = 0; i < shakeCount; i++) {
            double direction = (i % 2 == 0) ? 1 : -1;
            KeyValue keyValueX = new KeyValue(pokemonImage.translateXProperty(), direction * shakeIntensity);
            KeyFrame keyFrame = new KeyFrame(shakeDuration.multiply(i + 1), keyValueX);
            shakeTimeline.getKeyFrames().add(keyFrame);
        }

        return shakeTimeline;
    }

    /**
     * Creates a flashing animation to simulate a hit effect on the Pokémon.
     *
     * @param pokemonImage the {@link ImageView} of the Pokémon
     * @return a {@link Timeline} representing the flash
     */
    private static Timeline createFlashAnimation(ImageView pokemonImage) {
        return new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(pokemonImage.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(100), new KeyValue(pokemonImage.opacityProperty(), 0.5)),
                new KeyFrame(Duration.millis(200), new KeyValue(pokemonImage.opacityProperty(), 1.0))
        );
    }

    /**
     * Creates a smooth animation for updating the progress bar value of the health bar.
     *
     * @param pokemonHPBar the {@link ProgressBar} to animate
     * @param newBarValue  the target progress value
     * @return a {@link Timeline} representing the health bar animation
     */
    private static Timeline createAnimateHealthBarValue(ProgressBar pokemonHPBar, double newBarValue) {
        double durationSeconds = 1.0;
        KeyValue progressKV = new KeyValue(pokemonHPBar.progressProperty(), newBarValue, Interpolator.EASE_BOTH);
        KeyFrame progressKF = new KeyFrame(Duration.seconds(durationSeconds), progressKV);

        Timeline hpTimeline = new Timeline(progressKF);
        hpTimeline.setCycleCount(1);

        return hpTimeline;
    }

    /**
     * Creates an animation that updates the displayed HP value over time.
     *
     * @param pokemonHPText the {@link Text} node displaying HP
     * @param currentHP     the current HP value
     * @param newHP         the new HP value to animate toward
     * @return a {@link Timeline} for the HP text animation
     */
    private static Timeline createAnimateHPText(Text pokemonHPText, int currentHP, int newHP) {
        double durationSeconds = 1.0;
        final IntegerProperty animatedHP = new SimpleIntegerProperty(currentHP);

        animatedHP.addListener((obs, oldVal, newVal) -> pokemonHPText.setText(newVal.toString()));

        KeyValue hpKV = new KeyValue(animatedHP, newHP, Interpolator.EASE_BOTH);
        KeyFrame hpKF = new KeyFrame(Duration.seconds(durationSeconds), hpKV);

        return new Timeline(hpKF);
    }
}
