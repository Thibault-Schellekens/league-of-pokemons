package be.esi.prj.leagueofpokemons.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Controller for managing and displaying error messages in the League of Pokemons game.
 * <p>
 * This controller handles the visibility and text of an error pane and label, allowing error messages to be shown temporarily.
 * It provides methods for displaying, hiding, and customizing the duration of error messages.
 * </p>
 */
public class ErrorController {

    @FXML
    private Pane errorPane;
    @FXML
    private Label errorLabel;

    private double displayDuration = 5.0;

    /**
     * Initializes the controller by hiding the error pane and clearing the error label.
     */
    @FXML
    private void initialize() {
        hide();
    }

    /**
     * Displays an error message in the error pane for a specified duration.
     * <p>
     * The error message is shown in the error label, and the error pane becomes visible.
     * After the display duration has passed, the error message is hidden.
     * </p>
     *
     * @param message The error message to display.
     */
    public void displayError(String message) {
        errorPane.setVisible(true);
        errorLabel.setText(message);

        PauseTransition pause = new PauseTransition(Duration.seconds(displayDuration));
        pause.setOnFinished(e -> {
            hide();
        });
        pause.play();
    }

    private void hide() {
        errorPane.setVisible(false);
        errorLabel.setText("");
    }
}
