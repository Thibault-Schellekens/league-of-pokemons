package be.esi.prj.leagueofpokemons.animation;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.util.Duration;


public class ScannerAnimation {
    private static double startY;

    public static void addGlowingAnimation(Button btn, ImageView image) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), image);
        fadeIn.setToValue(1.0);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), image);
        fadeOut.setToValue(0.0);
        btn.setOnMouseEntered(e -> fadeIn.playFromStart());
        btn.setOnMouseExited(e -> fadeOut.playFromStart());
    }

    public static void scanningLineAnimation(Line line) {
        startY = line.getTranslateY();

        TranslateTransition transition = new TranslateTransition(Duration.millis(500), line);
        transition.setByY(395);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);

        line.getStyleClass().add("opacity-item");
        transition.play();
    }

    public static void stopScanningLineAnimation(Line line) {
        line.getStyleClass().remove("opacity-item");
        line.setTranslateY(startY);
    }
}
