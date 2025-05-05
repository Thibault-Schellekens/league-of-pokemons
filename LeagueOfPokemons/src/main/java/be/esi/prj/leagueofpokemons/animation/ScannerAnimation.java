package be.esi.prj.leagueofpokemons.animation;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class ScannerAnimation {
    private static TranslateTransition transition;

    private ScannerAnimation() {}

    public static void addGlowingAnimation(Button btn, ImageView image) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), image);
        fadeIn.setToValue(1.0);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), image);
        fadeOut.setToValue(0.0);
        btn.setOnMouseEntered(e -> fadeIn.playFromStart());
        btn.setOnMouseExited(e -> fadeOut.playFromStart());
    }

    public static void scanningLineAnimation(Line line) {
        transition = new TranslateTransition(Duration.millis(500), line);
        transition.setByY(395);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);

        line.getStyleClass().add("opacity-item");
        transition.play();
    }

    public static void stopScanningLineAnimation(Line line, double startY) {
        line.getStyleClass().remove("opacity-item");
        if (transition != null) {
            transition.stop();
        }
        line.setTranslateY(0);
        line.setLayoutY(startY);
    }

    public static void scanCompletedSuccess(ImageView image, Image newImage, Pane dropZone) {
        RotateTransition rotator = new RotateTransition(Duration.millis(750), image);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(90);
        rotator.setInterpolator(Interpolator.LINEAR);

        RotateTransition rotator2 = new RotateTransition(Duration.millis(750), image);
        rotator2.setAxis(Rotate.Y_AXIS);
        rotator2.setFromAngle(90);
        rotator2.setToAngle(0);
        rotator2.setInterpolator(Interpolator.LINEAR);


        rotator.setOnFinished(e -> {
            image.setImage(newImage);
            rotator2.play();
        });

        rotator2.setOnFinished(e -> {
            dropZone.getStyleClass().remove("no-display");
            dropZone.getStyleClass().add("drop-zone");
        });

        image.getStyleClass().remove("drop-image");
        dropZone.getStyleClass().add("no-display");
        dropZone.getStyleClass().remove("drop-zone");

        rotator.play();
    }
}
