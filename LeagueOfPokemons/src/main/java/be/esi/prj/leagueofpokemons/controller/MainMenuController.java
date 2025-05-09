package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.ModelException;
import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.view.ImageCache;
import javafx.scene.control.TextInputDialog;
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

    @FXML
    private void openSettings() {
        if (settingsMenuController != null) {
            settingsMenuController.showSettings();
        }
    }

    @FXML
    private void openLoadGame() {
        if (loadGameMenuController != null) {
            loadGameMenuController.showLoadGame();
        }
    }

    @FXML
    private void play() {
        SceneManager.switchScene(SceneView.HUB);
    }


    //TODO: GameManager quit method
    @FXML
    private void quit() {
        ImageCache.getInstance().shutdown();
        System.exit(0);
    }


    //TODO Already load the name if it has one
    @FXML
    private void save() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Game");
        dialog.setHeaderText("Enter the name of the game:");
        dialog.setContentText("Name:");

        dialog.showAndWait().ifPresent(name -> {
            try {
                GameManager.saveGame(name);
            } catch (ModelException e) {
                errorPanelController.displayError("Error saving game");
            }
        });
    }


    @FXML
    private void news() {

    }
}
