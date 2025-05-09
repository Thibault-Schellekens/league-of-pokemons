package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.view.ImageCache;
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


    //TODO: GameManager quit method
    public void quit() {
        ImageCache.getInstance().shutdown();
        System.exit(0);
    }

    public void save() {
        System.out.println("Saving game...");
        GameManager.saveGame();

    }

    public void load() {

    }

    public void news() {

    }
}
