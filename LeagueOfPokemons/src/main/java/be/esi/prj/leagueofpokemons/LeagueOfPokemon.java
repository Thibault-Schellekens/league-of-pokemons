package be.esi.prj.leagueofpokemons;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LeagueOfPokemon extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LeagueOfPokemon.class.getResource("mainMenu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1061, 663);
        stage.setTitle("League of Pokemons");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}