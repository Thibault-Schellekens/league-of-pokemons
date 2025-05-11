package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.ModelException;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * Controller for managing the hub view in the League of Pokemons game.
 * <p>
 * This controller is responsible for handling navigation between different scenes (e.g., scanner, collection, battle, and settings)
 * and displaying the current game stage information.
 * </p>
 */
public class HubController implements ControllerFXML {

    @FXML
    private SettingsController settingsMenuController;
    @FXML
    private ErrorController errorPanelController;

    @FXML
    private Text stageText;

    /**
     * Initializes the controller by setting the stage text to the current stage of the game.
     * This method is called when the hub view is loaded.
     */
    @Override
    public void init() {
        Game game = Game.getInstance();
        stageText.setText("Lvl. " + game.getCurrentStage());
    }

    /**
     * Opens the scanner scene.
     * <p>
     * This method is triggered when the user selects the "Scanner" option in the hub view.
     * </p>
     */
    @FXML
    private void openScanner() {
        SceneManager.switchScene(SceneView.SCANNER);
    }

    /**
     * Opens the collection scene.
     * <p>
     * This method is triggered when the user selects the "Collection" option in the hub view.
     * </p>
     */
    @FXML
    private void openCollection() {
        SceneManager.switchScene(SceneView.COLLECTION);
    }

    /**
     * Opens the battle scene.
     * <p>
     * This method is triggered when the user selects the "Battle" option in the hub view.
     * If there is an error when switching to the battle scene, an error message is displayed.
     * </p>
     */
    @FXML
    private void openBattle() {
        try {
            SceneManager.switchScene(SceneView.BATTLE);
        } catch (ModelException e) {
            errorPanelController.displayError(e.getMessage());
        }
    }

    /**
     * Opens the settings menu.
     * <p>
     * This method is triggered when the user selects the "Settings" option in the hub view.
     * If the settings menu controller is not null, the settings menu is shown.
     * </p>
     */
    @FXML
    private void openSettings() {
        if (settingsMenuController != null) {
            settingsMenuController.showSettings();
        }
    }

    /**
     * Returns to the main menu scene.
     * <p>
     * This method is triggered when the user selects the "Back" option in the hub view.
     * </p>
     */
    @FXML
    private void back() {
        SceneManager.switchScene(SceneView.MAINMENU);
    }
}
