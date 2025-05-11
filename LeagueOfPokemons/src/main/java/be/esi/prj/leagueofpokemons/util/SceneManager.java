package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.animation.SwapSceneAnimation;
import be.esi.prj.leagueofpokemons.controller.ControllerFXML;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Manages the scene transitions and settings for the application's user interface.
 * Responsible for loading and switching between different scenes (e.g., main menu, game view) with optional animations.
 */
public class SceneManager {
    private static Stage primaryStage;
    private static Scene scene;
    public static final int WIDTH = 1061;
    public static final int HEIGHT = 663;

    private static SettingsManager settingsManager;

    private SceneManager() {}

    /**
     * Initializes the primary stage and sets up the scene manager.
     * This method is called to start the application.
     */
    private static void setStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setResizable(false);
        settingsManager = SettingsManager.getInstance();

        primaryStage.setOnCloseRequest(event -> {
            GameManager.quit();
        });
    }

    /**
     * Starts the JavaFX application by initializing the primary stage
     * and switching to the main menu scene.
     */
    public static void start() {
        Platform.startup(() -> {
            primaryStage = new Stage();
            setStage(primaryStage);
            SceneManager.switchScene(SceneView.MAINMENU);
        });
    }

    /**
     * Switches to a specified scene, loading the corresponding FXML file.
     * If animation is enabled, transitions between scenes with an animation effect.
     *
     * @param view the scene to switch to (e.g., MAINMENU, BATTLE).
     */
    public static void switchScene(SceneView view) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/be/esi/prj/leagueofpokemons/" + view.name().toLowerCase() + "-view.fxml"));
            Parent root = fxmlLoader.load();
            ControllerFXML controller = fxmlLoader.getController();
            controller.init();

            if (scene == null || settingsManager.isSkipAnimation()) {
                scene = new Scene(root, WIDTH, HEIGHT);
                primaryStage.setScene(scene);
            } else {
                Pane nextRoot = (Pane) root;
                Pane mainRoot = new Pane();
                Node previousRoot = scene.getRoot();

                SwapSceneAnimation.swapSceneTransition(scene, mainRoot, nextRoot, previousRoot, settingsManager.getAnimationSpeed());
            }
            primaryStage.show();
        } catch (IOException e) {
        }
    }
}
