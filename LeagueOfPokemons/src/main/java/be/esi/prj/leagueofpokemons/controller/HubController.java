package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.ModelException;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class HubController implements ControllerFXML {
    @FXML
    private SettingsController settingsMenuController;
    @FXML
    private ErrorController errorPanelController;

    @FXML
    private Text stageText;

    @Override
    public void init() {
        Game game = Game.getInstance();
        stageText.setText("Lvl. " + game.getCurrentStage());
    }

    public void openScanner() {
        SceneManager.switchScene(SceneView.SCANNER);
    }

    public void openCollection() {
        SceneManager.switchScene(SceneView.COLLECTION);
    }

    public void openBattle() {
        try {
            SceneManager.switchScene(SceneView.BATTLE);
        } catch (ModelException e) {
            errorPanelController.displayError(e.getMessage());
        }
    }

    public void openSettings() {
        if (settingsMenuController != null) {
            settingsMenuController.showSettings();
        }
    }

    public void back() {
        SceneManager.switchScene(SceneView.MAINMENU);
    }
}
