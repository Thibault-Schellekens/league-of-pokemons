package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import javafx.fxml.FXML;

public class MainMenuController implements ControllerFXML{
    @FXML
    private SettingsController settingsMenuController;

    @Override
    public void init() {
    }

    public void openSettings() {
        if (settingsMenuController != null) {
            settingsMenuController.showSettings();
        }
    }

    public void play() {
        SceneManager.switchScene(SceneView.HUB);
    }

    public void quit() {
        System.exit(0);
    }

    public void save() {
        GameManager.getInstance().saveGame();

    }

    public void load() {

    }

    public void news() {

    }
}
