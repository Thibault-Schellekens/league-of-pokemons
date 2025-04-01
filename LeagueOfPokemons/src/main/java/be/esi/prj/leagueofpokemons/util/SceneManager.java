package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.animation.SwapSceneAnimation;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;
    private static Scene scene;
    private static final int WIDTH = 1061;
    private static final int HEIGHT = 663;

    private static SettingsManager settingsManager;

    private static void setStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setResizable(false);
        settingsManager = SettingsManager.getInstance();
    }

    public static void start() {
        Platform.startup(() -> {
            primaryStage = new Stage();
            setStage(primaryStage);
            SceneManager.switchScene(SceneView.MAINMENU);
        });
    }

    public static void switchScene(SceneView view) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/be/esi/prj/leagueofpokemons/" + view.name().toLowerCase() + "-view.fxml"));
            // Loading main menu
            if (scene == null || settingsManager.isSkipAnimation()) {
                Parent root = fxmlLoader.load();
                scene = new Scene(root, WIDTH, HEIGHT);
                primaryStage.setScene(scene);
            } else {
                Pane nextRoot = fxmlLoader.load();
                Pane mainRoot = new Pane();
                Node previousRoot = scene.getRoot();

                SwapSceneAnimation.swapSceneTransition(scene, mainRoot, nextRoot, previousRoot);
            }
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

