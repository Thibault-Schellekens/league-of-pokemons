package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import javafx.fxml.FXML;

public class MainMenuController implements ControllerFXML{
    @FXML
    private SettingsController settingsMenuController;
    GameManager gameManager;

    @Override
    public void init() {
        System.out.println("Initializing MainMenu Controller");
    }

    public void openSettings() {
        System.out.println("Settings");
        if (settingsMenuController != null) {
            System.out.println("Settings opened");
            settingsMenuController.showSettings();
        }
    }

    public void play() {
        System.out.println("Switching to Hub");
        SceneManager.switchScene(SceneView.HUB);
    }

    public void quit() {
        System.out.println("Quit");
    }

    public void save() {
        GameManager.getInstance().saveGame();

    }

    public void load() {
        System.out.println("Loading");
    }

    public void news() {
        System.out.println("News");
    }
}
