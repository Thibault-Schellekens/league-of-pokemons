package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.SettingsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

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

    public void initialize() {
        settingsManager = SettingsManager.getInstance();
        setupValues();
        hideSettings();
    }

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

    public void showSettings() {
        settingsPane.setVisible(true);
        settingsPane.getParent().toFront();
    }

    public void hideSettings() {
        settingsPane.setVisible(false);
        if (settingsPane.getParent() != null) {
            settingsPane.getParent().toBack();
        }
    }

    private void updateVolume(int volume) {
        volumeText.setText(volume + "%");
        settingsManager.setVolume(volume);
    }

    private void updateSkipAnimation(boolean skipAnimation) {
        settingsManager.setSkipAnimation(skipAnimation);
    }

    private void updateAnimationSpeed(double speed) {
        animationSpeedText.setText("x" + String.format("%.02f", speed));
        settingsManager.setAnimationSpeed(speed);
    }


}
