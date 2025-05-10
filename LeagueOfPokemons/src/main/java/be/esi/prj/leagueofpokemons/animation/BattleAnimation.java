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

public class BattleAnimation {

    private BattleAnimation() {
    }

    private static final AtomicBoolean isAnimationPlaying = new AtomicBoolean(false);
    private static final Queue<Animation> animationQueue = new LinkedList<>();

    private static Runnable updateFunction;

    public static void setFunction(Runnable function) {
        updateFunction = function;
    }

    public static boolean isAnimationPlaying() {
        return isAnimationPlaying.get();
    }

    public static void playDamageAnimation(ImageView pokemonImage, ProgressBar pokemonHPBar, double newBarValue, Text pokemonHPText, int newHP) {
        int currentHP = Integer.parseInt(pokemonHPText.getText());

        Timeline shakeAnimation = createShakeAnimation(pokemonImage);
        Timeline flashAnimation = createFlashAnimation(pokemonImage);
        Timeline healthBarValueAnimation = createAnimateHealthBarValue(pokemonHPBar, newBarValue);
        Timeline HPTextAnimation = createAnimateHPText(pokemonHPText, currentHP, newHP);

        ParallelTransition damageAnimations = new ParallelTransition(shakeAnimation, flashAnimation, healthBarValueAnimation, HPTextAnimation);

        queueAnimation(damageAnimations);
    }

    public static void playRestoreHealthAnimation(ProgressBar pokemonHPBar, double newBarValue, Text pokemonHPText, int newHP) {
        int currentHP = Integer.parseInt(pokemonHPText.getText());

        Timeline healthBarValueAnimation = createAnimateHealthBarValue(pokemonHPBar, newBarValue);
        Timeline HPTextAnimation = createAnimateHPText(pokemonHPText, currentHP, newHP);

        ParallelTransition restoreAnimations = new ParallelTransition(healthBarValueAnimation, HPTextAnimation);

        queueAnimation(restoreAnimations);
    }

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

    private static Timeline createFlashAnimation(ImageView pokemonImage) {
        return new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(pokemonImage.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(100), new KeyValue(pokemonImage.opacityProperty(), 0.5)),
                new KeyFrame(Duration.millis(200), new KeyValue(pokemonImage.opacityProperty(), 1.0))
        );
    }

    private static Timeline createAnimateHealthBarValue(ProgressBar pokemonHPBar, double newBarValue) {
        double durationSeconds = 1.0;
        KeyValue progressKV = new KeyValue(pokemonHPBar.progressProperty(), newBarValue, Interpolator.EASE_BOTH);
        KeyFrame progressKF = new KeyFrame(Duration.seconds(durationSeconds), progressKV);

        Timeline hpTimeline = new Timeline(progressKF);
        hpTimeline.setCycleCount(1);

        return hpTimeline;
    }

    private static Timeline createAnimateHPText(Text pokemonHPText, int currentHP, int newHP) {
        double durationSeconds = 1.0;
        final IntegerProperty animatedHP = new SimpleIntegerProperty(currentHP);

        animatedHP.addListener((obs, oldVal, newVal) -> pokemonHPText.setText(newVal.toString()));

        KeyValue hpKV = new KeyValue(animatedHP, newHP, Interpolator.EASE_BOTH);
        KeyFrame hpKF = new KeyFrame(Duration.seconds(durationSeconds), hpKV);

        Timeline numberTimeline = new Timeline(hpKF);

        return numberTimeline;
    }

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

    private static void queueAnimation(Animation animation) {
        animationQueue.add(animation);
        playNextAnimation();
    }

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


}
