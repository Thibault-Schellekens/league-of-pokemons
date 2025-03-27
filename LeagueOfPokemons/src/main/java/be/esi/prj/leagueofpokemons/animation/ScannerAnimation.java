package be.esi.prj.leagueofpokemons.animation;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.util.Duration;


public class ScannerAnimation {

    public static void addGlowingAnimation(Button btn, ImageView image) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), image);
        fadeIn.setToValue(1.0);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), image);
        fadeOut.setToValue(0.0);
        btn.setOnMouseEntered(e -> fadeIn.playFromStart());
        btn.setOnMouseExited(e -> fadeOut.playFromStart());
    }

    public static void scanningLineAnimation(Line line) {
        double startY = line.getTranslateY();

        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), line);
        transition.setByY(395);
        transition.setAutoReverse(true);
        transition.setCycleCount(3);

        line.getStyleClass().add("opacity-item");
        transition.play();

        transition.setOnFinished(e -> {
            line.getStyleClass().remove("opacity-item");
            line.setTranslateY(startY);
        });
    }
}
