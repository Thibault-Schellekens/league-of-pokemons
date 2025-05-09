package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.ModelException;
import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.view.ImageCache;
import javafx.fxml.FXML;

public class MainMenuController implements ControllerFXML{
    @FXML
    private SettingsController settingsMenuController;
    @FXML
    private LoadGameController loadGameMenuController;
    @FXML
    private ErrorController errorPanelController;

    @Override
    public void init() {
    }

    public void openSettings() {
        if (settingsMenuController != null) {
            settingsMenuController.showSettings();
        }
    }

    public void openLoadGame() {
        if (loadGameMenuController != null) {
            loadGameMenuController.showLoadGame();
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
        try {
            GameManager.saveGame();
        } catch (ModelException e) {
            errorPanelController.displayError("Error saving game");
        }

    }

    public void news() {

    }
}
