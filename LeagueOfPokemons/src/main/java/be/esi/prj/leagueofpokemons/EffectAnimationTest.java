package be.esi.prj.leagueofpokemons;

import be.esi.prj.leagueofpokemons.animation.EffectAnimation;
import be.esi.prj.leagueofpokemons.model.core.Effect;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Test application for previewing effect animations
 */
public class EffectAnimationTest extends Application {

    private ImageView pokemonImageView;
    private Pane animationPane;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Animation area
        animationPane = new Pane();
        animationPane.setPrefSize(600, 400);
        animationPane.setStyle("-fx-background-color: lightgray;");

        // Create an ImageView with a placeholder
        pokemonImageView = new ImageView();
        pokemonImageView.setFitWidth(200);
        pokemonImageView.setFitHeight(200);
        pokemonImageView.setLayoutX(200);
        pokemonImageView.setLayoutY(100);

        // Try to load a placeholder image
        try {
            // Replace with an actual path to a Pokemon image if available
            Image placeholderImage = new Image(getClass().getResourceAsStream("/be/esi/prj/leagueofpokemons/pics/battle/bulbasaur.png"));
            pokemonImageView.setImage(placeholderImage);
        } catch (Exception e) {
            System.err.println("Could not load placeholder image: " + e.getMessage());
            // Create a basic placeholder
            pokemonImageView.setStyle("-fx-background-color: darkgray;");
        }

        animationPane.getChildren().add(pokemonImageView);
        root.setCenter(animationPane);

        // Controls
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));

        // Effect type selection
        ComboBox<Effect.EffectType> effectTypeComboBox = new ComboBox<>();
        effectTypeComboBox.getItems().addAll(Effect.EffectType.values());
        effectTypeComboBox.setValue(Effect.EffectType.BURN);
        effectTypeComboBox.setPrefWidth(150);

        // Play button
        Button playButton = new Button("Play Animation");
        playButton.setOnAction(event -> {
            Effect.EffectType selectedEffect = effectTypeComboBox.getValue();
            EffectAnimation.playEffectAnimation(pokemonImageView, animationPane, selectedEffect);
        });

        // Reset button
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            // Remove all particles and effects
            animationPane.getChildren().clear();
            animationPane.getChildren().add(pokemonImageView);
            pokemonImageView.setEffect(null);
            pokemonImageView.setOpacity(1.0);
            pokemonImageView.setLayoutX(200);
            pokemonImageView.setLayoutY(100);
        });

        HBox buttonBox = new HBox(10, playButton, resetButton);
        controls.getChildren().addAll(
                new javafx.scene.control.Label("Select Effect:"),
                effectTypeComboBox,
                buttonBox
        );

        root.setBottom(controls);

        // Create scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Effect Animation Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}