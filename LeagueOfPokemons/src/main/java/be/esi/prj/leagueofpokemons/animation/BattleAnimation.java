package be.esi.prj.leagueofpokemons.animation;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BattleAnimation {

    public static void playDamageAnimation(ImageView pokemonImage, ProgressBar pokemonHPBar, double newBarValue, Text pokemonHPText, int newHP) {
        int currentHP = Integer.parseInt(pokemonHPText.getText());

        playShakeAnimation(pokemonImage);
        playFlashAnimation(pokemonImage);
        animateHealthBarValue(pokemonHPBar, newBarValue);
        animateHPText(pokemonHPText, currentHP, newHP);
    }

    public static void playRestoreHealthAnimation(ProgressBar pokemonHPBar, double newBarValue, Text pokemonHPText, int newHP) {
        int currentHP = Integer.parseInt(pokemonHPText.getText());

        animateHealthBarValue(pokemonHPBar, newBarValue);
        animateHPText(pokemonHPText, currentHP, newHP);
    }

    private static void playShakeAnimation(ImageView pokemonImage) {
        Timeline shakeTimeline = new Timeline();

        double shakeIntensity = 10.0;
        int shakeCount = 6;
        Duration shakeDuration = Duration.millis(75);

        for (int i = 0; i < shakeCount; i++) {
            double direction = (i % 2 == 0) ? 1 : -1;
            KeyValue keyValueX = new KeyValue(pokemonImage.translateXProperty(),
                    direction * shakeIntensity);

            KeyFrame keyFrame = new KeyFrame(
                    shakeDuration.multiply(i + 1),
                    keyValueX
            );

            shakeTimeline.getKeyFrames().add(keyFrame);
        }

        shakeTimeline.play();
    }

    private static void playFlashAnimation(ImageView pokemonImage) {
        Timeline colorTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(pokemonImage.opacityProperty(), 1.0)
                ),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(pokemonImage.opacityProperty(), 0.5)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(pokemonImage.opacityProperty(), 1.0)
                )
        );

        colorTimeline.play();
    }

    private static void animateHealthBarValue(ProgressBar pokemonHPBar, double newBarValue) {
        double durationSeconds = 1.0;
        KeyValue progressKV = new KeyValue(pokemonHPBar.progressProperty(), newBarValue, Interpolator.EASE_BOTH);
        KeyFrame progressKF = new KeyFrame(Duration.seconds(durationSeconds), progressKV);

        Timeline hpTimeline = new Timeline(progressKF);
        hpTimeline.setCycleCount(1);
        hpTimeline.play();
    }

    private static void animateHPText(Text pokemonHPText, int currentHP, int newHP) {
        double durationSeconds = 1.0;
        final IntegerProperty animatedHP = new SimpleIntegerProperty(currentHP);

        animatedHP.addListener((obs, oldVal, newVal) -> pokemonHPText.setText(newVal.toString()));

        KeyValue hpKV = new KeyValue(animatedHP, newHP, Interpolator.EASE_BOTH);
        KeyFrame hpKF = new KeyFrame(Duration.seconds(durationSeconds), hpKV);

        Timeline numberTimeline = new Timeline();
        numberTimeline.getKeyFrames().add(hpKF);
        numberTimeline.play();
    }
}
