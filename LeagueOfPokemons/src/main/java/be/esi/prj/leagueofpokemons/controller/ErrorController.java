package be.esi.prj.leagueofpokemons.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ErrorController {

    @FXML
    private Pane errorPane;
    @FXML
    private Label errorLabel;

    private double displayDuration = 5.0;

    public void initialize() {
        errorPane.setVisible(false);
    }

    public void displayError(String message) {
        errorPane.setVisible(true);
        errorLabel.setText(message);

        PauseTransition pause = new PauseTransition(Duration.seconds(displayDuration));
        pause.setOnFinished(e -> {
            errorPane.setVisible(false);
            errorLabel.setText("");
        });
        pause.play();
    }

    public void setDisplayDuration(double displayDuration) {
        this.displayDuration = displayDuration;
    }

    public void hideError() {
        errorPane.setVisible(false);
        errorLabel.setText("");
    }
}
