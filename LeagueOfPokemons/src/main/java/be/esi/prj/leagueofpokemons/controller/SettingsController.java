package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.SettingsManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.control.CheckBox;

public class SettingsController {

    SettingsManager settingsManager;

    @FXML
    private Slider volumeSlider;
    @FXML
    private CheckBox skipAnimationCheckbox;
    @FXML
    private Slider animationSpeedSlider;
    @FXML
    private Pane settingsPane;

    public void initialize() {
        System.out.println("Initializing Settings Component Controller");
        settingsManager = SettingsManager.getInstance();
        setupValues();
        hideSettings();
        settingsPane.setOnMouseClicked(event -> System.out.println("Settings Pane Clicked!"));
    }

    private void setupValues() {
        volumeSlider.setValue(settingsManager.getVolume() * 100);
        skipAnimationCheckbox.setSelected(settingsManager.isSkipAnimation());
        animationSpeedSlider.setValue(settingsManager.getAnimationSpeed() * 100);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            settingsManager.setVolume(newValue.doubleValue() / 100);
            updateVolume();
        });

        skipAnimationCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            settingsManager.setSkipAnimation(newValue);
            updateSkipAnimation();
        });

        animationSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            settingsManager.setAnimationSpeed(newValue.doubleValue() / 100);
            updateAnimationSpeed();

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
//        settingsPane.getParent().toFront();
    }

    private void updateVolume() {
        System.out.println("Updating Volume");
    }

    private void updateSkipAnimation() {
        System.out.println("Updating Skip Animation");
    }

    private void updateAnimationSpeed() {
        System.out.println("Updating Animation Speed");
    }


}
