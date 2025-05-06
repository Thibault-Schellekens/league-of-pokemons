package be.esi.prj.leagueofpokemons.animation;

import be.esi.prj.leagueofpokemons.model.core.Pokemon;
import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BattleAnimation {

    private BattleAnimation() {
    }

    private static Pokemon pokemon;

    public static void setPokemon(Pokemon pokemon) {
        BattleAnimation.pokemon = pokemon;
    }

    public static void playDamageAnimation(ImageView pokemonImage, ProgressBar pokemonHPBar, double newBarValue, Text pokemonHPText, int newHP) {
        int currentHP = Integer.parseInt(pokemonHPText.getText());

        Timeline shakeAnimation = playShakeAnimation(pokemonImage);
        Timeline flashAnimation = playFlashAnimation(pokemonImage);
        Timeline healthBarValueAnimation = animateHealthBarValue(pokemonHPBar, newBarValue);
        Timeline HPTextAnimation = animateHPText(pokemonHPText, currentHP, newHP);

        ParallelTransition allAnimations = new ParallelTransition(shakeAnimation, flashAnimation, healthBarValueAnimation, HPTextAnimation);

        // Make sure to update with the right Pokémon infos.
        // The current Pokémon might have changed during the animation
        if (pokemon != null) {
            allAnimations.setOnFinished(e -> {
                pokemonHPText.setText(String.valueOf(currentHP));
                pokemonHPBar.setProgress((double) pokemon.getCurrentHP() / pokemon.getMaxHP());
                pokemon = null;
            });
        }

        allAnimations.play();

    }

    public static void playRestoreHealthAnimation(ProgressBar pokemonHPBar, double newBarValue, Text pokemonHPText, int newHP) {
        int currentHP = Integer.parseInt(pokemonHPText.getText());

        Timeline healthBarValueAnimation = animateHealthBarValue(pokemonHPBar, newBarValue);
        Timeline HPTextAnimation = animateHPText(pokemonHPText, currentHP, newHP);

        ParallelTransition allAnimations = new ParallelTransition(healthBarValueAnimation, HPTextAnimation);
        allAnimations.play();
    }

    private static Timeline playShakeAnimation(ImageView pokemonImage) {
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

        return shakeTimeline;
    }

    private static Timeline playFlashAnimation(ImageView pokemonImage) {
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

        return colorTimeline;
    }

    private static Timeline animateHealthBarValue(ProgressBar pokemonHPBar, double newBarValue) {
        double durationSeconds = 1.0;
        KeyValue progressKV = new KeyValue(pokemonHPBar.progressProperty(), newBarValue, Interpolator.EASE_BOTH);
        KeyFrame progressKF = new KeyFrame(Duration.seconds(durationSeconds), progressKV);

        Timeline hpTimeline = new Timeline(progressKF);
        hpTimeline.setCycleCount(1);

        return hpTimeline;
    }

    private static Timeline animateHPText(Text pokemonHPText, int currentHP, int newHP) {
        double durationSeconds = 1.0;
        final IntegerProperty animatedHP = new SimpleIntegerProperty(currentHP);

        animatedHP.addListener((obs, oldVal, newVal) -> pokemonHPText.setText(newVal.toString()));

        KeyValue hpKV = new KeyValue(animatedHP, newHP, Interpolator.EASE_BOTH);
        KeyFrame hpKF = new KeyFrame(Duration.seconds(durationSeconds), hpKV);

        Timeline numberTimeline = new Timeline();
        numberTimeline.getKeyFrames().add(hpKF);

        return numberTimeline;
    }
}
