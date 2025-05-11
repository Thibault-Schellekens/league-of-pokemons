package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.ModelException;
import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.util.audio.AudioManager;
import be.esi.prj.leagueofpokemons.util.audio.AudioSound;
import be.esi.prj.leagueofpokemons.view.ImageCache;
import javafx.scene.control.TextInputDialog;
import javafx.fxml.FXML;

/**
 * Controller for managing the main menu in the League of Pokemons game.
 * <p>
 * This controller handles the main menu actions such as opening settings, loading a saved game, saving the game,
 * and quitting the game.
 * </p>
 */
public class MainMenuController implements ControllerFXML {

    @FXML
    private SettingsController settingsMenuController;
    @FXML
    private LoadGameController loadGameMenuController;
    @FXML
    private ErrorController errorPanelController;

    private final AudioManager audioManager;

    /**
     * Constructor for MainMenuController.
     * Initializes the audio manager instance.
     */
    public MainMenuController() {
        audioManager = AudioManager.getInstance();
    }

    /**
     * Initializes the main menu controller. Currently does not contain any specific logic.
     */
    @Override
    public void init() {
    }

    /**
     * Opens the settings menu.
     * <p>
     * This method displays the settings menu if the settings menu controller is initialized.
     * </p>
     */
    @FXML
    private void openSettings() {
        if (settingsMenuController != null) {
            settingsMenuController.showSettings();
        }
    }

    /**
     * Opens the load game menu.
     * <p>
     * This method displays the load game screen if the load game menu controller is initialized.
     * </p>
     */
    @FXML
    private void openLoadGame() {
        if (loadGameMenuController != null) {
            loadGameMenuController.showLoadGame();
        }
    }

    /**
     * Starts a new game by switching to the hub scene.
     * <p>
     * This method is triggered when the user chooses to play a new game.
     * </p>
     */
    @FXML
    private void play() {
        SceneManager.switchScene(SceneView.HUB);
    }

    //todo quit method GameManager
    /**
     * Quits the game and shuts down any necessary resources.
     * <p>
     * This method is triggered when the user chooses to quit the game. It shuts down all resources
     * and exits the application.
     * </p>
     */
    @FXML
    private void quit() {
        GameManager.quit();
    }

    /**
     * Prompts the user to enter a name for the saved game and saves the game.
     * <p>
     * This method displays a dialog to ask the user for a name, and then it saves the game with the provided name.
     * If an error occurs during saving, an error message is displayed.
     * </p>
     */
    @FXML
    private void save() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Game");
        dialog.setHeaderText("Enter the name of the game:");
        dialog.setContentText("Name:");

        dialog.showAndWait().ifPresent(name -> {
            try {
                GameManager.saveGame(name);
                audioManager.playSound(AudioSound.SAVE_GAME);
            } catch (ModelException e) {
                errorPanelController.displayError("Error saving game");
            }
        });
    }

    /**
     * Placeholder method for news functionality.
     * <p>
     * This method currently does not contain any logic but can be implemented in the future to display game news.
     * </p>
     */
    @FXML
    private void news() {

    }
}
