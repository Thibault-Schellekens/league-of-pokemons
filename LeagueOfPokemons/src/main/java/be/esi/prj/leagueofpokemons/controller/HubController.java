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

    private Game game;

    @Override
    public void init() {
        System.out.println("Initializing Hub Controller");
        game = Game.getInstance();
        stageText.setText("Lvl. " + game.getCurrentStage());
    }

    public void openScanner() {
        System.out.println("Opening Scanner");
        SceneManager.switchScene(SceneView.SCANNER);
    }

    public void openCollection() {
        System.out.println("Opening Collection");
        SceneManager.switchScene(SceneView.COLLECTION);
    }

    public void openBattle() {
        try {
            System.out.println("Opening Battle");
            SceneManager.switchScene(SceneView.BATTLE);
        } catch (ModelException e) {
            System.out.println(e.getMessage());
            errorPanelController.displayError(e.getMessage());
        }
    }

    public void openSettings() {
        System.out.println("Settings");
        if (settingsMenuController != null) {
            System.out.println("Settings opened");
            settingsMenuController.showSettings();
        }
    }

    public void back() {
        SceneManager.switchScene(SceneView.MAINMENU);
    }
}
