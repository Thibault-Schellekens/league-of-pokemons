package be.esi.prj.leagueofpokemons.animation;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Utility class providing various scanning animations for the League of Pokémons interface.
 */
public class ScannerAnimation {

    private static TranslateTransition transition;

    /**
     * Private constructor to prevent instantiation.
     */
    private ScannerAnimation() {}

    /**
     * Adds a glowing fade animation to an image associated with a button's hover state.
     *
     * @param btn   the {@link Button} that triggers the animation on hover
     * @param image the {@link ImageView} that will fade in and out on hover
     */
    public static void addGlowingAnimation(Button btn, ImageView image) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), image);
        fadeIn.setToValue(1.0);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), image);
        fadeOut.setToValue(0.0);
        btn.setOnMouseEntered(e -> fadeIn.playFromStart());
        btn.setOnMouseExited(e -> fadeOut.playFromStart());
    }

    /**
     * Starts an infinite vertical scanning animation on the given line.
     *
     * @param line the {@link Line} to animate
     */
    public static void scanningLineAnimation(Line line) {
        transition = new TranslateTransition(Duration.millis(500), line);
        transition.setByY(395);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);

        line.getStyleClass().add("opacity-item");
        transition.play();
    }

    /**
     * Stops the scanning animation and resets the line's position.
     *
     * @param line   the {@link Line} whose animation should be stopped
     * @param startY the Y position to reset the line to
     */
    public static void stopScanningLineAnimation(Line line, double startY) {
        line.getStyleClass().remove("opacity-item");
        if (transition != null) {
            transition.stop();
        }
        line.setTranslateY(0);
        line.setLayoutY(startY);
    }

    /**
     * Performs a rotation animation to simulate a successful scan and updates the image accordingly.
     * Also toggles visibility of the drop zone.
     *
     * @param image     the {@link ImageView} to rotate and update
     * @param newImage  the new {@link Image} to display after the scan
     * @param dropZone  the {@link Pane} representing the drop zone to be shown after the scan
     */
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
