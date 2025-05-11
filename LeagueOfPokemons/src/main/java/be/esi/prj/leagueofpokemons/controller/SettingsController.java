package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.SettingsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

/**
 * Controller for managing the settings screen in the application.
 * This controller allows users to adjust settings like volume, animation speed, and whether to skip animations.
 * <p>
 * It uses a {@link SettingsManager} to persist and retrieve settings data.
 * </p>
 */
public class SettingsController {

    private SettingsManager settingsManager;

    @FXML
    private Slider volumeSlider;
    @FXML
    private Text volumeText;
    @FXML
    private CheckBox skipAnimationCheckbox;
    @FXML
    private Slider animationSpeedSlider;
    @FXML
    private Text animationSpeedText;
    @FXML
    private Pane settingsPane;

    /**
     * Initializes the settings controller, setting up the slider values, checkbox state,
     * and adding listeners for any changes.
     * <p>
     * The values are loaded from the {@link SettingsManager}.
     * </p>
     */
    @FXML
    private void initialize() {
        settingsManager = SettingsManager.getInstance();
        setupValues();
        hideSettings();
    }

    /**
     * Sets up the initial values for the settings based on the {@link SettingsManager} data.
     * <p>
     * This includes setting the initial volume, skip animation option, and animation speed.
     * Listeners are added to update these values in real-time when the user interacts with the UI components.
     * </p>
     */
    private void setupValues() {
        volumeSlider.setValue(settingsManager.getVolume());
        updateVolume(settingsManager.getVolume());
        skipAnimationCheckbox.setSelected(settingsManager.isSkipAnimation());
        animationSpeedSlider.setValue(((settingsManager.getAnimationSpeed() - 1) / (settingsManager.getMaxAnimationSpeed() - 1)) * 100);
        updateAnimationSpeed(settingsManager.getAnimationSpeed());

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int volume = newValue.intValue();
            updateVolume(volume);
        });

        skipAnimationCheckbox.selectedProperty().addListener((observable, oldValue, newValue) ->
                updateSkipAnimation(newValue));

        animationSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double speed = 1 + (newValue.doubleValue() / 100) * (settingsManager.getMaxAnimationSpeed() - 1);
            updateAnimationSpeed(speed);
        });
    }

    /**
     * Displays the settings pane in the UI and brings it to the front of its parent container.
     */
    public void showSettings() {
        settingsPane.setVisible(true);
        settingsPane.getParent().toFront();
    }

    /**
     * Hides the settings pane from the UI and sends it to the back of its parent container.
     */
    @FXML
    private void hideSettings() {
        settingsPane.setVisible(false);
        if (settingsPane.getParent() != null) {
            settingsPane.getParent().toBack();
        }
    }

    /**
     * Updates the volume level and displays it in the volume text field.
     *
     * @param volume The volume level (0-100).
     */
    private void updateVolume(int volume) {
        volumeText.setText(volume + "%");
        settingsManager.setVolume(volume);
    }

    /**
     * Updates the setting for skipping animations.
     *
     * @param skipAnimation A boolean indicating whether animations should be skipped.
     */
    private void updateSkipAnimation(boolean skipAnimation) {
        settingsManager.setSkipAnimation(skipAnimation);
    }

    /**
     * Updates the animation speed and displays it in the animation speed text field.
     *
     * @param speed The animation speed multiplier.
     */
    private void updateAnimationSpeed(double speed) {
        animationSpeedText.setText("x" + String.format("%.02f", speed));
        settingsManager.setAnimationSpeed(speed);
    }
}
